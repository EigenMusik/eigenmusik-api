package it.com.eigenmusik.controllers;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.Track;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.repository.AccountRepository;
import com.eigenmusik.services.repository.TrackRepository;
import com.eigenmusik.services.repository.UserProfileRepository;
import it.com.eigenmusik.IntegrationTestsBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        while(i < numberOfTracks) {
            Track track = factory.manufacturePojo(Track.class);
            track.setCreatedBy(userProfile);
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
