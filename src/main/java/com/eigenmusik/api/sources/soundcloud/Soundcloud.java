package com.eigenmusik.api.sources.soundcloud;

import com.eigenmusik.api.exceptions.SourceAuthenticationException;
import com.eigenmusik.api.sources.*;
import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.tracks.TrackSource;
import com.eigenmusik.api.tracks.TrackStreamUrl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Soundcloud implementation of the source abstract.
 */
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
            SoundcloudGateway soundcloudGateway,
            SoundcloudAccessTokenRepository soundcloudAccessTokenRepository,
            SoundcloudUserRepository soundcloudUserRepository,
            SoundcloudConfiguration soundcloudConfiguration) {
        this.soundcloudGateway = soundcloudGateway;
        this.soundcloudAccessTokenRepository = soundcloudAccessTokenRepository;
        this.soundcloudUserRepository = soundcloudUserRepository;
        this.soundcloudConfiguration = soundcloudConfiguration;
    }

    @Override
    public SourceAccount getAccount(SourceAccountAuthentication auth) throws SourceAuthenticationException {
        try {
            SoundcloudAccessToken soundcloudAccessToken = soundcloudGateway.exchangeToken(auth.getCode());
            SoundcloudUser soundcloudUser = soundcloudGateway.getMe(soundcloudAccessToken);
            soundcloudUser.setAccessToken(soundcloudAccessToken);

            soundcloudAccessTokenRepository.save(soundcloudAccessToken);
            soundcloudUserRepository.save(soundcloudUser);

            SourceAccount account = new SourceAccount();
            account.setUri(soundcloudUser.getId());
            account.setSource(SourceType.SOUNDCLOUD);
            return account;
        } catch (IOException e) {
            throw new SourceAuthenticationException(SourceType.SOUNDCLOUD);
        }
    }

    @Override
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

    @Override
    public SourceType getType() {
        return SourceType.SOUNDCLOUD;
    }

    @Override
    public TrackStreamUrl getStreamUrl(Track track) {
        log.info(track.getTrackSource().getOwner().getUri());
        SoundcloudUser soundcloudUser = soundcloudUserRepository.findOne(Long.valueOf(track.getTrackSource().getOwner().getUri()));
        return new TrackStreamUrl(soundcloudGateway.getStreamUrl(Long.valueOf(track.getTrackSource().getUri()), soundcloudUser.getAccessToken()));
    }

    /**
     * Static helper function to map a Soundcloud track to an EigenMusik entity.
     *
     * @param t
     * @param account
     * @return Track
     */
    private static Track mapToTrack(SoundcloudTrack t, SourceAccount account) {
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
