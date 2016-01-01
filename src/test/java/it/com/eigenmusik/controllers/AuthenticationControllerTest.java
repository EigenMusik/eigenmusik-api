package it.com.eigenmusik.controllers;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.Track;
import com.eigenmusik.services.repository.AccountRepository;
import com.eigenmusik.services.repository.TrackRepository;
import com.eigenmusik.services.repository.UserProfileRepository;
import it.com.eigenmusik.IntegrationTestsBase;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().isOk());

        // Can't register twice with the same username.
        account.setEmail("anotherEmail@home.com");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());

        // Can't register twice with the same email.
        account.setEmail("anEmail@test.com");
        account.setName("anotherName");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());
    }
}
