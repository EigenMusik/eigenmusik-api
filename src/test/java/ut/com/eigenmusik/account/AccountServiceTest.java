package ut.com.eigenmusik.account;

import com.eigenmusik.account.Account;
import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.account.AccountService;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private AccountRepository accountRepository;
    private UserProfileRepository userProfileRepository;
    private AccountService accountService;

    @Before
    public void setUp() throws IOException {
        userProfileRepository = Mockito.mock(UserProfileRepository.class);
        accountRepository = Mockito.mock(AccountRepository.class);
        accountService = new AccountService(accountRepository, userProfileRepository);
    }

    @Test
    public void testRegistration() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);
        when(accountRepository.findByName(account.getName())).thenReturn(null);
        assertEquals(accountService.register(account), account);
        verify(accountRepository, times(1)).save(Mockito.any(Account.class));
        verify(userProfileRepository, times(1)).save(Mockito.any(UserProfile.class));
    }

    @Test(expected = EmailExistsException.class)
    public void testDuplicateRegistrationByEmail() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(account);
        when(accountRepository.findByName(account.getName())).thenReturn(null);
        accountService.register(account);
    }

    @Test(expected = UsernameExistsException.class)
    public void testDuplicateRegistrationByUsername() throws EmailExistsException, UsernameExistsException {
        Account account = factory.manufacturePojo(Account.class);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);
        when(accountRepository.findByName(account.getName())).thenReturn(account);
        accountService.register(account);
    }
}
