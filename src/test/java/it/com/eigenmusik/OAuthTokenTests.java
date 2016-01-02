package it.com.eigenmusik;

import com.eigenmusik.tracks.TrackRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuthTokenTests extends IntegrationTestsBase {

    @Autowired
    TrackRepository trackRepository;

    @Before
    public void Setup() {

    }

    @Test
    public void testNotAuthorized() throws Exception {
        mvc.perform(get("/rest/tracks/")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthorized() throws Exception {

        mvc.perform(get("/rest/tracks/").header("Authorization", "Bearer " + tokenForClient("user1"))).andExpect(status().isOk());
    }
}
