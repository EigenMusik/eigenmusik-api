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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timcoulson on 15/12/2015.
 */
@Component
@ConfigurationProperties(prefix = "soundcloud")
public class SoundcloudService {

    private static Logger log = Logger.getLogger(SoundcloudService.class);

    private String clientId;

    private String clientSecret;

    private String redirectUrl;
    @Autowired
    private SoundcloudAccessTokenRepository accessTokenRepository;
    @Autowired
    private SoundcloudUserRepository soundcloudUserRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackRepository trackRepository;

    public SoundcloudService() {
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Track> getTracks(SoundcloudUser user) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SoundcloudTrack[]> responseEntity = restTemplate.getForEntity("http://api.soundcloud.com/users/" + user.getId() + "/favorites?client_id=" + clientId, SoundcloudTrack[].class);

        SoundcloudTrack[] tracks = responseEntity.getBody();
        List<SoundcloudTrack> tracksList = Arrays.asList(tracks);

        return tracksList.stream().map(t -> mapToTrack(t)).collect(Collectors.toList());
    }

    public SoundcloudUser getMe(SoundcloudAccessToken token) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SoundcloudUser> responseEntity = restTemplate.getForEntity("http://api.soundcloud.com/me?oauth_token=" + token.getAccessToken(), SoundcloudUser.class);

        SoundcloudUser user = responseEntity.getBody();
        return user;
    }

    private SoundcloudAccessToken exchangeToken(String code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("client_id", clientId);
        bodyMap.add("client_secret", clientSecret);
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("redirect_uri", redirectUrl);
        bodyMap.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
        String accessTokenString = restTemplate.postForObject("https://api.soundcloud.com/oauth2/token", entity, String.class);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(accessTokenString, SoundcloudAccessToken.class);
    }

    public boolean connectAccount(String code, UserProfile user) {
        SoundcloudAccessToken accessToken;
        try {
            accessToken = this.exchangeToken(code);
            ;
        } catch (Exception e) {
            return false;
        }

        // Get user entity from soundcloud.
        SoundcloudUser soundcloudUser = this.getMe(accessToken);
        soundcloudUser.setCreatedBy(user);
        soundcloudUser.setAccessToken(accessToken);

        accessTokenRepository.save(accessToken);
        soundcloudUserRepository.save(soundcloudUser);

        // Retrieve tracks and associate with eigenmusik account.

        List<Track> tracks = this.getTracks(soundcloudUser);
        tracks.forEach(track -> track.setCreatedBy(user));
        tracks.forEach(track1 -> track1.setCreatedOn(Calendar.getInstance().getTime()));
        trackRepository.save(tracks);

        return true;
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
