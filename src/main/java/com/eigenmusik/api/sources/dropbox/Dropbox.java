package com.eigenmusik.api.sources.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.eigenmusik.api.config.EigenMusikConfiguration;
import com.eigenmusik.api.sources.*;
import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.tracks.TrackSource;
import com.eigenmusik.api.tracks.TrackStreamUrl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DropBox implementation of the Source abstract.
 */
@Service
public class Dropbox extends Source {

    private static Logger log = Logger.getLogger(Dropbox.class);

    private final DbxRequestConfig config;
    private final DropboxUserRepository dropboxUserRepository;
    private final DropboxAccessTokenRepository dropboxAccessTokenRepository;
    private final DbxWebAuth webAuth;

    @Autowired
    public Dropbox(
            DropboxUserRepository dropboxUserRepository,
            DropboxConfiguration dropboxConfiguration,
            DropboxAccessTokenRepository dropboxAccessTokenRepository
    ) {
        this.dropboxUserRepository = dropboxUserRepository;
        this.dropboxAccessTokenRepository = dropboxAccessTokenRepository;

        DbxAppInfo appInfo = new DbxAppInfo(dropboxConfiguration.getClientId(), dropboxConfiguration.getClientSecret());
        this.config = new DbxRequestConfig(EigenMusikConfiguration.APP_NAME, Locale.getDefault().toString());

        DbxSessionStore dbxSessionStore = new DropboxUserSessionStore();
        this.webAuth = new DbxWebAuth(config, appInfo, dropboxConfiguration.getRedirectUrl(), dbxSessionStore);
    }

    @Override
    public TrackStreamUrl getStreamUrl(Track track) {
        DropboxUser dropboxUser = dropboxUserRepository.findOne(track.getTrackSource().getOwner().getUri());
        DropboxAccessToken accessToken = dropboxUser.getAccessToken();

        DbxClientV1 dbxClient = new DbxClientV1(config, accessToken.getAccessToken());
        try {
            return new TrackStreamUrl(dbxClient.createTemporaryDirectUrl(track.getTrackSource().getUri()).url);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SourceAccount getAccount(SourceAccountAuthentication auth) throws SourceAuthenticationException {
        try {
            Map<String, String[]> params = auth.toParameterMap();

            // Inject a fake csrf token to authenticate the client, I know this is naughty :)
            // TODO figure out session handling with Spring.
            params.put("state", new String[]{DropboxUserSessionStore.fakeCsrfToken});
            DbxAuthFinish authFinish = webAuth.finish(params);
            DbxClientV2 dbxClient = new DbxClientV2(config, authFinish.accessToken);

            // Instantiate the dropbox access token.
            DropboxAccessToken dropboxAccessToken = new DropboxAccessToken(authFinish.accessToken);
            DropboxUser dropboxUser = new DropboxUser(dbxClient.users.getCurrentAccount());
            dropboxUser.setAccessToken(dropboxAccessToken);

            // Save the token and dropbox user account.
            dropboxAccessTokenRepository.save(dropboxAccessToken);
            dropboxUserRepository.save(dropboxUser);

            // Instantiate and return the EigenMusik source account object.
            SourceAccount account = new SourceAccount();
            account.setUri(dropboxUser.getId());
            account.setSource(SourceType.DROPBOX);

            return account;
        } catch (DbxWebAuth.BadRequestException | DbxException | DbxWebAuth.ProviderException | DbxWebAuth.CsrfException | DbxWebAuth.NotApprovedException | DbxWebAuth.BadStateException e) {
            // TODO bit of a code smell catching all these messages, perhaps it's a third party inevitability.
            throw new SourceAuthenticationException(SourceType.DROPBOX);
        }
    }

    @Override
    public List<Track> getTracks(SourceAccount account) {
        DropboxUser dropboxUser = dropboxUserRepository.findOne(account.getUri());
        DropboxAccessToken accessToken = dropboxUser.getAccessToken();

        // Using the V1 client, couldn't get V2 to work :(
        // TODO further investigate DropBox V2.
        DbxClientV1 clientv1 = new DbxClientV1(config, accessToken.getAccessToken());

        try {
            return clientv1.searchFileAndFolderNames("/", "mp3").stream().map(mp3 -> mapToTrack(mp3, account)).collect(Collectors.toList());
        } catch (DbxException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Help classer to map a DropBox track entity to an EigenMusik one.
     *
     * @param dbxEntry
     * @param sourceAccount
     * @return
     */
    private Track mapToTrack(DbxEntry dbxEntry, SourceAccount sourceAccount) {
        TrackSource trackSource = new TrackSource();
        trackSource.setUri(dbxEntry.path);
        trackSource.setSource(SourceType.DROPBOX);
        trackSource.setOwner(sourceAccount);

        Track track = new Track();
        track.setName(dbxEntry.path);
        // TODO how do we handle files without artist and track names?
        track.setArtist("Drive File");
        track.setTrackSource(trackSource);

        return track;
    }

    @Override
    public String getName() {
        return "Dropbox";
    }

    @Override
    public String getAuthUrl() {
        return webAuth.start();
    }

    public SourceType getType() {
        return SourceType.DROPBOX;
    }
}
