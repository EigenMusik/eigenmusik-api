package com.eigenmusik.services;

import com.eigenmusik.domain.Track;
import com.eigenmusik.services.sources.soundcloud.SoundcloudService;
import com.eigenmusik.services.sources.soundcloud.SoundcloudTrack;
import com.eigenmusik.services.sources.soundcloud.SoundcloudUser;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by timcoulson on 27/12/2015.
 */
public class SoundcloudServiceTest {

    MockRestServiceServer mockServer;
    SoundcloudService soundcloudService;

    String clientId = "12345";

    public String sampleJsonData() throws IOException {
        ClassPathResource res = new ClassPathResource("favouritesResponse.json");
        return IOUtils.toString(res.getInputStream());
    }

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        soundcloudService = new SoundcloudService();
        soundcloudService.setRestTemplate(restTemplate);
        soundcloudService.setClientId(clientId);
        soundcloudService.setClientSecret("456");
    }

    @Test
    public void testGetMessage() throws IOException {

        Long userId = 123L;

        mockServer.expect(requestTo("http://api.soundcloud.com/users/" + userId + "/favorites?client_id=" + clientId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body(sampleJsonData()).contentType(MediaType.APPLICATION_JSON));

        SoundcloudUser user = new SoundcloudUser();
        user.setId(userId);

        List<Track> responseTracks = soundcloudService.getTracks(user);

        mockServer.verify();
        assertThat(responseTracks.get(0).getArtist().getName(), containsString("Data Transmission"));
        assertThat(responseTracks.get(0).getName(), containsString("Mix of the Day: Adeline"));

    }
}
