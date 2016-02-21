package ut.com.eigenmusik.api.user;

import com.eigenmusik.api.common.ValidationException;
import com.eigenmusik.api.user.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private UserService userService;

    @Before
    public void setUp() throws IOException {
        userProfileRepository = Mockito.mock(UserProfileRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userProfileRepository, userRepository);
    }

    @Test
    public void testRegistration() throws ValidationException {
        User user = factory.manufacturePojo(User.class);
        user.setUserProfile(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByName(user.getName())).thenReturn(null);
        assertEquals(userService.register(user), user);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verify(userProfileRepository, times(1)).save(Mockito.any(UserProfile.class));
    }

    @Test(expected = ValidationException.class)
    public void testDuplicateRegistrationByEmail() throws ValidationException {
        User user = factory.manufacturePojo(User.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findByName(user.getName())).thenReturn(null);
        userService.register(user);
    }

    @Test(expected = ValidationException.class)
    public void testDuplicateRegistrationByUsername() throws ValidationException {
        User user = factory.manufacturePojo(User.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByName(user.getName())).thenReturn(user);
        userService.register(user);
    }

    @Test
    public void testGetUserByName() throws UserDoesntExistException {
        User user = factory.manufacturePojo(User.class);
        when(userRepository.findByName(user.getName())).thenReturn(user);
        assertEquals(userService.getByUsername(user.getName()), user);
    }

    @Test(expected = UserDoesntExistException.class)
    public void testGetUserByNameNotExisting() throws UserDoesntExistException {
        User user = factory.manufacturePojo(User.class);
        when(userRepository.findByName(user.getName())).thenReturn(null);
        userService.getByUsername(user.getName());
    }

}
