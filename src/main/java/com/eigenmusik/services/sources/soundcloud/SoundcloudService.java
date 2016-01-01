package com.eigenmusik.services.sources.soundcloud;

import com.eigenmusik.domain.*;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.sources.Source;
import com.eigenmusik.services.sources.SourceService;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudAccessToken;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudTrack;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudUser;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudAccessTokenRepository;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudTrackRepository;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "soundcloud")
public class SoundcloudService implements SourceService {

    private static Logger log = Logger.getLogger(SoundcloudService.class);
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private SoundcloudUserRepository soundcloudUserRepository;
    private SoundcloudTrackRepository soundcloudTrackRepository;
    private TrackRepository trackRepository;

    @Autowired
    public SoundcloudService(
            SoundcloudGateway soundcloudGateway,
            SoundcloudAccessTokenRepository soundcloudAccessTokenRepository,
            SoundcloudTrackRepository soundcloudTrackRepository,
            SoundcloudUserRepository soundcloudUserRepository,
            TrackRepository trackRepository) {
        this.soundcloudGateway = soundcloudGateway;
        this.soundcloudAccessTokenRepository = soundcloudAccessTokenRepository;
        this.soundcloudTrackRepository = soundcloudTrackRepository;
        this.soundcloudUserRepository = soundcloudUserRepository;
        this.trackRepository = trackRepository;
    }

    public boolean connectAccount(String code, UserProfile user) {
        SoundcloudAccessToken accessToken;
        try {
            accessToken = soundcloudGateway.exchangeToken(code);
            // Get user entity from soundcloud.
            SoundcloudUser soundcloudUser = soundcloudGateway.getMe(accessToken);
            soundcloudUser.setCreatedBy(user);
            soundcloudUser.setAccessToken(accessToken);

            List<SoundcloudTrack> soundcloudTracks = soundcloudGateway.getTracks(soundcloudUser);

            List<Track> tracks = soundcloudTracks.stream().map(t -> mapToTrack(t)).collect(Collectors.toList());
            tracks.forEach(track -> track.setCreatedBy(user));
            tracks.forEach(track -> track.setCreatedOn(Calendar.getInstance().getTime()));

            soundcloudAccessTokenRepository.save(accessToken);
            soundcloudUserRepository.save(soundcloudUser);

            trackRepository.save(tracks);
            soundcloudTrackRepository.save(soundcloudTracks);

            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public StreamUrl getStreamUrl(Track track) {
        SoundcloudTrack soundcloudTrack = soundcloudTrackRepository.findByTrack(track);
        return new StreamUrl(soundcloudGateway.getStreamUrl(soundcloudTrack));
    }

    // TODO should be bind?
    private Track mapToTrack(SoundcloudTrack t) {
        t.setTrack(new Track(
                t.getTitle(),
                new Artist(t.getArtist()),
                new Album("An album"),
                Source.SOUNDCLOUD,
                12345678L));
        return t.getTrack();
    }

}
