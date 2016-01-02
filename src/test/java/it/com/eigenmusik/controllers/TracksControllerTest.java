package it.com.eigenmusik.controllers;

import com.eigenmusik.account.Account;
import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackRepository;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileRepository;
import it.com.eigenmusik.IntegrationTestsBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TracksControllerTest extends IntegrationTestsBase {

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    AccountRepository accountRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<Track> tracks;

    @Before
    public void Setup() {

    }

    @Test
    public void testGetTracks() throws Exception {
        int i = 0;
        int numberOfTracks = 20;
        Account account = factory.manufacturePojo(Account.class);
        account.setId(null);
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);
        userProfile.setAccount(account);
        userProfile.setId(null);

        tracks = new ArrayList<>();

        while (i < numberOfTracks) {
            Track track = factory.manufacturePojo(Track.class);
            track.setCreatedBy(userProfile);
            track.setId(null);
            track.setTrackSource(null);
            tracks.add(i, track);
            i++;
        }

        accountRepository.save(account);
        userProfileRepository.save(userProfile);
        trackRepository.save(tracks);

        mvc.perform(get("/rest/tracks/").header("Authorization", "Bearer " + tokenForClient(account.getName())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(numberOfTracks)))
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}
