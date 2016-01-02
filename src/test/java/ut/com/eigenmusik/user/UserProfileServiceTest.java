package ut.com.eigenmusik.user;

import com.eigenmusik.account.Account;
import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileRepository;
import com.eigenmusik.user.UserProfileService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserProfileServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private AccountRepository accountRepository;
    private UserProfileRepository userProfileRepository;
    private UserProfileService userService;

    @Before
    public void setUp() throws IOException {
        userProfileRepository = Mockito.mock(UserProfileRepository.class);
        accountRepository = Mockito.mock(AccountRepository.class);
        userService = new UserProfileService(userProfileRepository, accountRepository);
    }

    @Test
    public void testGetUserProfileByString() throws UserDoesntExistException {
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByName(userProfile.getDisplayName())).thenReturn(account);
        when(userProfileRepository.findByAccount(account)).thenReturn(userProfile);
        assertEquals(userService.getUserProfile(userProfile.getDisplayName()), userProfile);
    }

    @Test(expected = UserDoesntExistException.class)
    public void testGetUserProfileByStringNotExisting() throws UserDoesntExistException {
        String username = "aUserName";
        when(accountRepository.findByName(username)).thenReturn(null);
        userService.getUserProfile(username);
    }

    @Test(expected = UserDoesntExistException.class)
    public void testGetUserProfileByEmailNotExisting() throws UserDoesntExistException {
        String email = "aBadEmail@home.com";
        when(accountRepository.findByEmail(email)).thenReturn(null);
        userService.getUserProfileByEmail(email);
    }

    @Test
    public void testGetUserProfileByEmail() throws UserDoesntExistException {
        Account account = factory.manufacturePojo(Account.class);
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(account);
        when(userProfileRepository.findByAccount(account)).thenReturn(userProfile);
        assertEquals(userService.getUserProfileByEmail(account.getEmail()), userProfile);
    }

    @Test(expected = UserDoesntExistException.class)
    public void testUserProfileNotExisting() throws UserDoesntExistException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(account);
        when(userProfileRepository.findByAccount(account)).thenReturn(null);
        userService.getUserProfileByEmail(account.getEmail());
    }

}
