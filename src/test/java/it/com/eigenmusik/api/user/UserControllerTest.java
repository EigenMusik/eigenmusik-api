package it.com.eigenmusik.api.user;

import com.eigenmusik.api.user.User;
import it.com.eigenmusik.api.IntegrationTestsBase;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends IntegrationTestsBase {

    @Test
    public void testRegisterNewUser() throws Exception {
        User account = new User();
        account.setEmail("anEmail@test.com");
        account.setName("aUserName");
        account.setPassword("aPassword");

        String accountJson = new ObjectMapper().writeValueAsString(account);

        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().isOk());

        // Can't messages.register twice with the same username.
        account.setEmail("anotherEmail@home.com");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());

        // Can't messages.register twice with the same email.
        account.setEmail("anEmail@test.com");
        account.setName("anotherName");
        accountJson = new ObjectMapper().writeValueAsString(account);
        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(status().is4xxClientError());
    }
}
