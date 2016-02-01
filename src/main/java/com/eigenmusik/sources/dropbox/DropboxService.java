package com.eigenmusik.sources.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.sources.SourceType;
import com.eigenmusik.sources.SourceAccount;
import com.eigenmusik.sources.SourceAccountRepository;
import com.eigenmusik.sources.SourceService;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackStreamUrl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class DropboxService extends SourceService {

    private static Logger log = Logger.getLogger(DropboxService.class);
    private DropboxUserRepository dropboxUserRepository;

    private DropboxConfiguration dropboxConfiguration;
    private DropboxAccessTokenRepository dropboxAccessTokenRepository;

    @Autowired
    public DropboxService(SourceAccountRepository sourceAccountRepository, DropboxUserRepository dropboxUserRepository, DropboxConfiguration dropboxConfiguration, DropboxAccessTokenRepository dropboxAccessTokenRepository) {
        super(sourceAccountRepository);
        this.dropboxUserRepository = dropboxUserRepository;
        this.dropboxConfiguration = dropboxConfiguration;
        this.dropboxAccessTokenRepository = dropboxAccessTokenRepository;
    }

    @Override
    public TrackStreamUrl getStreamUrl(Track track) {
        return null;
    }

    @Override
    public SourceAccount getAccount(String authCode) throws SourceAuthenticationException {


        // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = "8ajr3w5onf908mt";
        final String APP_SECRET = "bxaff9bphoaikmg";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        webAuth.start();
        try {
            DbxAuthFinish authFinish = webAuth.finish(authCode);
            String accessToken = authFinish.accessToken;
            DbxClientV2 client = new DbxClientV2(config, accessToken);

            DropboxAccessToken dropboxAccessToken = new DropboxAccessToken(accessToken);

            DropboxUser dropboxUser = new DropboxUser(client.users.getCurrentAccount());
            dropboxUser.setAccessToken(dropboxAccessToken);

            dropboxAccessTokenRepository.save(dropboxAccessToken);
            dropboxUserRepository.save(dropboxUser);

            SourceAccount account = new SourceAccount();
            account.setUri(dropboxUser.getId());
            account.setSource(SourceType.SOUNDCLOUD);
            return account;

        } catch (DbxException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public List<Track> getTracks(SourceAccount account) {
        return null;
    }

    @Override
    public String getName() {
        return "DropBox";
    }

    @Override
    public String getAuthUrl() {
        return null;
    }

    public SourceType getType() {
        return SourceType.SOUNDCLOUD;
    }
}
