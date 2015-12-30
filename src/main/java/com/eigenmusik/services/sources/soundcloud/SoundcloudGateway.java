package com.eigenmusik.services.sources.soundcloud;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Component
public class SoundcloudGateway {

    private static Logger log = Logger.getLogger(SoundcloudGateway.class);
    private final SoundcloudConfiguration config;

    private RestTemplate restTemplate;

    @Autowired
    public SoundcloudGateway(RestTemplate restTemplate, SoundcloudConfiguration config) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public List<SoundcloudTrack> getTracks(SoundcloudUser user) {
        ResponseEntity<SoundcloudTrack[]> responseEntity = restTemplate.getForEntity("http://api.soundcloud.com/users/" + user.getId() + "/favorites?client_id=" + config.getClientId(), SoundcloudTrack[].class);

        SoundcloudTrack[] tracks = responseEntity.getBody();
        List<SoundcloudTrack> tracksList = Arrays.asList(tracks);

        return tracksList;
    }

    public SoundcloudUser getMe(SoundcloudAccessToken token) {
        ResponseEntity<SoundcloudUser> responseEntity = restTemplate.getForEntity("http://api.soundcloud.com/me?oauth_token=" + token.getAccessToken(), SoundcloudUser.class);

        SoundcloudUser user = responseEntity.getBody();
        return user;
    }

    public SoundcloudAccessToken exchangeToken(String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("client_id", config.getClientId());
        bodyMap.add("client_secret", config.getClientSecret());
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("redirect_uri", config.getRedirectUrl());
        bodyMap.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
        String accessTokenString = restTemplate.postForObject("https://api.soundcloud.com/oauth2/token", entity, String.class);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(accessTokenString, SoundcloudAccessToken.class);
    }

}
