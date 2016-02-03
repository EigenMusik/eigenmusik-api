package com.eigenmusik.sources.soundcloud;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.sources.Source;
import com.eigenmusik.sources.SourceAccount;
import com.eigenmusik.sources.SourceAccountRepository;
import com.eigenmusik.sources.SourceType;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackSource;
import com.eigenmusik.tracks.TrackStreamUrl;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties(prefix = "soundcloud")
public class Soundcloud extends Source {

    private static Logger log = Logger.getLogger(Soundcloud.class);
    private SoundcloudConfiguration soundcloudConfiguration;
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private SoundcloudUserRepository soundcloudUserRepository;

    @Autowired
    public Soundcloud(
            SourceAccountRepository sourceAccountRepository,
            SoundcloudGateway soundcloudGateway,
            SoundcloudAccessTokenRepository soundcloudAccessTokenRepository,
            SoundcloudUserRepository soundcloudUserRepository,
            SoundcloudConfiguration soundcloudConfiguration) {
        super(sourceAccountRepository);
        this.soundcloudGateway = soundcloudGateway;
        this.soundcloudAccessTokenRepository = soundcloudAccessTokenRepository;
        this.soundcloudUserRepository = soundcloudUserRepository;
        this.soundcloudConfiguration = soundcloudConfiguration;
    }

    public SourceAccount getAccount(String uri) throws SourceAuthenticationException {
        try {
            // Retrieve code from Uri
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(uri), "UTF-8");
            String code = params.stream().filter(p -> p.getName().equals("code")).findFirst().get().getValue();

            SoundcloudAccessToken soundcloudAccessToken = soundcloudGateway.exchangeToken(code);
            SoundcloudUser soundcloudUser = soundcloudGateway.getMe(soundcloudAccessToken);
            soundcloudUser.setAccessToken(soundcloudAccessToken);

            soundcloudAccessTokenRepository.save(soundcloudAccessToken);
            soundcloudUserRepository.save(soundcloudUser);

            SourceAccount account = new SourceAccount();
            account.setUri(soundcloudUser.getId());
            account.setSource(SourceType.SOUNDCLOUD);
            return account;
        } catch (IOException | URISyntaxException e) {
            SourceAuthenticationException sourceAuthenticationException = new SourceAuthenticationException();
            sourceAuthenticationException.setSource(SourceType.SOUNDCLOUD);
            throw sourceAuthenticationException;
        }
    }

    public List<Track> getTracks(SourceAccount account) {
        List<Track> tracks = new ArrayList<>();
        SoundcloudUser soundcloudUser = soundcloudUserRepository.findOne(account.getUri());
        try {
            List<SoundcloudTrack> soundcloudTracks = soundcloudGateway.getTracks(soundcloudUser);

            tracks = soundcloudTracks.stream().map(t -> mapToTrack(t, account)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    @Override
    public String getName() {
        return "Soundcloud";
    }

    @Override
    public String getAuthUrl() {
        return "https://soundcloud.com/connect?client_id=" + soundcloudConfiguration.getClientId()
                + "&response_type=code" + "&redirect_uri=" + soundcloudConfiguration.getRedirectUrl();
    }

    public SourceType getType() {
        return SourceType.SOUNDCLOUD;
    }

    public TrackStreamUrl getStreamUrl(Track track) {
        log.info(track.getTrackSource().getOwner().getUri());
        SoundcloudUser soundcloudUser = soundcloudUserRepository.findOne(Long.valueOf(track.getTrackSource().getOwner().getUri()));
        return new TrackStreamUrl(soundcloudGateway.getStreamUrl(Long.valueOf(track.getTrackSource().getUri()), soundcloudUser.getAccessToken()));
    }

    // TODO where should this live?
    private Track mapToTrack(SoundcloudTrack t, SourceAccount account) {
        TrackSource trackSource = new TrackSource();
        trackSource.setUri(t.getSoundcloudId().toString());
        trackSource.setSource(SourceType.SOUNDCLOUD);
        trackSource.setOwner(account);

        Track track = new Track();
        track.setName(t.getTitle());
        track.setArtist(t.getArtist());
        track.setTrackSource(trackSource);
        return track;
    }

}
