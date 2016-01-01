package ut.com.eigenmusik.services;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.services.repository.AccountRepository;
import com.eigenmusik.services.repository.UserProfileRepository;
import com.eigenmusik.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by timcoulson on 27/12/2015.
 */
public class UserServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private AccountRepository accountRepository;
    private UserProfileRepository userProfileRepository;
    private UserService userService;

    @Before
    public void setUp() throws IOException {
        userProfileRepository = Mockito.mock(UserProfileRepository.class);
        accountRepository = Mockito.mock(AccountRepository.class);
        userService = new UserService();
        userService.setUserProfileRepository(userProfileRepository);
        userService.setAccountRepository(accountRepository);
    }

    @Test
    public void testGetUserProfileByString() throws UserDoesntExistException {
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByName(userProfile.getDisplayName())).thenReturn(account);
        when(userProfileRepository.findByAccount(account)).thenReturn(userProfile);
        assertEquals(userService.getUserProfile(userProfile.getDisplayName()), userProfile);
    }

    @Test(expected=UserDoesntExistException.class)
    public void testGetUserProfileByStringNotExisting() throws UserDoesntExistException {
        String username = "aUserName";
        when(accountRepository.findByName(username)).thenReturn(null);
        userService.getUserProfile(username);
    }

    @Test(expected=UserDoesntExistException.class)
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

    @Test(expected=UserDoesntExistException.class)
    public void testUserProfileNotExisting() throws UserDoesntExistException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(account);
        when(userProfileRepository.findByAccount(account)).thenReturn(null);
        userService.getUserProfileByEmail(account.getEmail());
    }

    @Test
    public void testRegistration() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);
        when(accountRepository.findByName(account.getName())).thenReturn(null);
        assertEquals(userService.register(account), account);
        verify(accountRepository, times(1)).save(Mockito.any(Account.class));
        verify(userProfileRepository, times(1)).save(Mockito.any(UserProfile.class));
    }

    @Test(expected=EmailExistsException.class)
    public void testDuplicateRegistrationByEmail() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(account);
        when(accountRepository.findByName(account.getName())).thenReturn(null);
        userService.register(account);
    }

    @Test(expected=UsernameExistsException.class)
    public void testDuplicateRegistrationByUsername() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);
        when(accountRepository.findByName(account.getName())).thenReturn(account);
        userService.register(account);
    }
}
