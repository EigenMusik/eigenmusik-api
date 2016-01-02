package com.eigenmusik.sources.soundcloud;

import com.eigenmusik.sources.soundcloud.json.SoundcloudAccessTokenJson;
import com.eigenmusik.sources.soundcloud.json.SoundcloudTrackJson;
import com.eigenmusik.sources.soundcloud.json.SoundcloudUserJson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SoundcloudGateway {

    private static Logger log = Logger.getLogger(SoundcloudGateway.class);
    private final SoundcloudConfiguration config;
    private final String REST_API = "https://api.soundcloud.com";
    private RestTemplate restTemplate;

    @Autowired
    public SoundcloudGateway(RestTemplate restTemplate, SoundcloudConfiguration config) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public List<SoundcloudTrack> getTracks(SoundcloudUser user) throws IOException, HttpClientErrorException {
        String requestUrl = REST_API + "/users/" + user.getSoundcloudId() + "/favorites?client_id=" + config.getClientId();
        return Arrays.asList(
                restTemplate.getForEntity(requestUrl, SoundcloudTrackJson[].class).getBody()
        ).stream().map(t -> new SoundcloudTrack(t, user)).collect(Collectors.toList());
    }

    public SoundcloudUser getMe(SoundcloudAccessToken token) throws HttpClientErrorException {
        return new SoundcloudUser(restTemplate
                .getForEntity(REST_API + "/me?oauth_token=" + token.getAccessToken(), SoundcloudUserJson.class)
                .getBody());
    }

    public SoundcloudAccessToken exchangeToken(String code) throws IOException, HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("client_id", config.getClientId());
        bodyMap.add("client_secret", config.getClientSecret());
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("redirect_uri", config.getRedirectUrl());
        bodyMap.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
        String accessTokenString = restTemplate.postForObject(REST_API + "/oauth2/token", entity, String.class);
        ObjectMapper mapper = new ObjectMapper();

        return new SoundcloudAccessToken(mapper.readValue(accessTokenString, SoundcloudAccessTokenJson.class));
    }

    public String getStreamUrl(Long soundcloudId, SoundcloudAccessToken accessToken) {
        String requestUrl = REST_API + "/tracks/" + soundcloudId + "?oauth_token=" + accessToken.getAccessToken();
        log.info("Request url = " + requestUrl);
        SoundcloudTrackJson soundcloudTrackJson = restTemplate.getForEntity(requestUrl, SoundcloudTrackJson.class).getBody();
        return soundcloudTrackJson.getStreamUrl() + "?oauth_token=" + accessToken.getAccessToken();
    }

}
