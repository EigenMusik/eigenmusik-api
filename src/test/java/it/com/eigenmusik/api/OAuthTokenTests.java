package it.com.eigenmusik.api;

import com.eigenmusik.api.common.ValidationException;
import com.eigenmusik.api.user.User;
import com.eigenmusik.api.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuthTokenTests extends IntegrationTestsBase {

    @Autowired
    UserService userService;

    @Before
    public void Setup() throws ValidationException {

    }

    @Test
    public void testNotAuthorized() throws Exception {
        mvc.perform(get("/tracks/")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthorized() throws Exception, ValidationException {
        User user = new User();
        user.setName("aUser");
        user.setEmail("anEmail@email.com");
        user.setPassword("password");
        userService.register(user);

        mvc.perform(get("/tracks/").header("Authorization", "Bearer " + tokenForClient("aUser"))).andExpect(status().isOk());
    }
}
