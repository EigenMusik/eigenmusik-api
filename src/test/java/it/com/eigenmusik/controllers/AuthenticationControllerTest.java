package it.com.eigenmusik.controllers;

import com.eigenmusik.account.Account;
import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackRepository;
import com.eigenmusik.user.UserProfileRepository;
import it.com.eigenmusik.IntegrationTestsBase;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends IntegrationTestsBase {

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
    public void testRegisterNewUser() throws Exception {
        Account account = new Account();
        account.setEmail("anEmail@test.com");
        account.setName("aUserName");
        account.setPassword("aPassword");

        String accountJson = new ObjectMapper().writeValueAsString(account);

        mvc.perform(post("/account/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().isOk());

        // Can't register twice with the same username.
        account.setEmail("anotherEmail@home.com");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/account/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());

        // Can't register twice with the same email.
        account.setEmail("anEmail@test.com");
        account.setName("anotherName");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/account/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());
    }
}
