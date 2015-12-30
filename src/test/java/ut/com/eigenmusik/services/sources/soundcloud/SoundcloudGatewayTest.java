package ut.com.eigenmusik.services.sources.soundcloud;

import com.eigenmusik.services.sources.soundcloud.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Created by timcoulson on 27/12/2015.
 */
public class SoundcloudGatewayTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private MockRestServiceServer mockServer;
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudConfiguration config;

    public String favouritesData() throws IOException {
        ClassPathResource res = new ClassPathResource("fixtures/soundcloud/favouritesResponse.json");
        return IOUtils.toString(res.getInputStream());
    }

    public String meData() throws IOException {
        ClassPathResource res = new ClassPathResource("fixtures/soundcloud/meResponse.json");
        return IOUtils.toString(res.getInputStream());
    }

    public String oauth2TokenData() throws IOException {
        ClassPathResource res = new ClassPathResource("fixtures/soundcloud/oauth2TokenResponse.json");
        return IOUtils.toString(res.getInputStream());
    }

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        config = new SoundcloudConfiguration();
        config.setClientId("anId");
        config.setClientSecret("aSecret");
        config.setRedirectUrl("http://redirectUrl:1234/hello.html");
        soundcloudGateway = new SoundcloudGateway(restTemplate, config);
    }

    @Test
    public void testGetTracks() throws IOException {
        Long userId = 123L;

        mockServer.expect(requestTo("http://api.soundcloud.com/users/" + userId + "/favorites?client_id=" + config.getClientId()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body(favouritesData()).contentType(MediaType.APPLICATION_JSON));

        SoundcloudUser user = new SoundcloudUser();
        user.setId(userId);

        List<SoundcloudTrack> responseTracks = soundcloudGateway.getTracks(user);

        mockServer.verify();
        assertThat(responseTracks.get(0).getUser().getUsername(), is("Data Transmission"));
        assertThat(responseTracks.get(0).getTitle(), is("Mix of the Day: Adeline"));
    }

    @Test(expected=HttpClientErrorException.class)
    public void testFailedGetTracks() throws IOException {
        Long userId = 123L;

        mockServer.expect(requestTo("http://api.soundcloud.com/users/" + userId + "/favorites?client_id=" + config.getClientId()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        SoundcloudUser user = new SoundcloudUser();
        user.setId(userId);

        soundcloudGateway.getTracks(user);
        mockServer.verify();
    }


    @Test
    public void testGetMe() throws IOException {
        Long userId = 123L;
        SoundcloudAccessToken accessToken = factory.manufacturePojo(SoundcloudAccessToken.class);
        accessToken.setAccessToken("token");

        mockServer.expect(requestTo("http://api.soundcloud.com/me?oauth_token=" +  accessToken.getAccessToken()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body(meData()).contentType(MediaType.APPLICATION_JSON));

        SoundcloudUser user = soundcloudGateway.getMe(accessToken);

        mockServer.verify();
        assertThat(user.getUsername(), is("Johannes Wagener"));
    }

    @Test(expected=HttpClientErrorException.class)
    public void testFailedGetMe() throws IOException {
        Long userId = 123L;
        SoundcloudAccessToken accessToken = factory.manufacturePojo(SoundcloudAccessToken.class);
        accessToken.setAccessToken("token");

        mockServer.expect(requestTo("http://api.soundcloud.com/me?oauth_token=" + accessToken.getAccessToken()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        soundcloudGateway.getMe(accessToken);
        mockServer.verify();
    }

    @Test
    public void testExchangeToken() throws IOException {
        String code = "12345";

        mockServer.expect(requestTo("https://api.soundcloud.com/oauth2/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("client_id=" + config.getClientId() + "&client_secret=" + config.getClientSecret() + "&grant_type=authorization_code&redirect_uri=" + URLEncoder.encode(config.getRedirectUrl(), "UTF-8") + "&code=" + code))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withStatus(HttpStatus.OK).body(oauth2TokenData()).contentType(MediaType.APPLICATION_JSON));

        SoundcloudAccessToken accessToken = soundcloudGateway.exchangeToken(code);

        mockServer.verify();
        assertThat(accessToken.getAccessToken(), is("04u7h-4cc355-70k3n"));
    }

    @Test(expected=HttpClientErrorException.class)
    public void testFailedExchangeToken() throws IOException {
        String code = "12345";

        mockServer.expect(requestTo("https://api.soundcloud.com/oauth2/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("client_id=" + config.getClientId() + "&client_secret=" + config.getClientSecret() + "&grant_type=authorization_code&redirect_uri=" + URLEncoder.encode(config.getRedirectUrl(), "UTF-8") + "&code=" + code))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        soundcloudGateway.exchangeToken(code);
        mockServer.verify();
    }
}
