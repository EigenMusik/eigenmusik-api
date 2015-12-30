package com.eigenmusik.services.sources.soundcloud;

import com.eigenmusik.domain.Album;
import com.eigenmusik.domain.Artist;
import com.eigenmusik.domain.Track;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.UserService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "soundcloud")
public class SoundcloudService {

    private static Logger log = Logger.getLogger(SoundcloudService.class);
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private SoundcloudUserRepository soundcloudUserRepository;
    private TrackRepository trackRepository;

    @Autowired
    public SoundcloudService(
            SoundcloudGateway soundcloudGateway,
            SoundcloudAccessTokenRepository soundcloudAccessTokenRepository,
            SoundcloudUserRepository soundcloudUserRepository,
            TrackRepository trackRepository) {
        this.soundcloudGateway = soundcloudGateway;
        this.soundcloudAccessTokenRepository = soundcloudAccessTokenRepository;
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
            List<Track> tracks = soundcloudGateway.getTracks(soundcloudUser).stream().map(t -> mapToTrack(t)).collect(Collectors.toList());
            tracks.forEach(track -> track.setCreatedBy(user));
            tracks.forEach(track1 -> track1.setCreatedOn(Calendar.getInstance().getTime()));
            soundcloudAccessTokenRepository.save(accessToken);
            soundcloudUserRepository.save(soundcloudUser);
            trackRepository.save(tracks);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Track mapToTrack(SoundcloudTrack t) {
        return new Track(
                t.getTitle(),
                new Artist(t.getUser().getUsername()),
                new Album("An album"),
                t.getId().toString(),
                "SOUNDCLOUD",
                12345678L);
    }

}
