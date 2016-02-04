package ut.com.eigenmusik.sources.soundcloud;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.sources.SourceAccountAuthentication;
import com.eigenmusik.sources.SourceType;
import com.eigenmusik.sources.SourceAccountRepository;
import com.eigenmusik.sources.soundcloud.*;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoundcloudServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();

    private SoundcloudGateway soundcloudGateway;
    private SoundcloudUserRepository soundcloudUserRepository;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private Soundcloud soundcloudService;
    private SourceAccountRepository sourceAccountRepository;
    private SoundcloudConfiguration soundcloudConfiguration;

    @Before
    public void setUp() throws IOException {
        soundcloudGateway = mock(SoundcloudGateway.class);
        soundcloudUserRepository = mock(SoundcloudUserRepository.class);
        soundcloudAccessTokenRepository = mock(SoundcloudAccessTokenRepository.class);
        sourceAccountRepository = mock(SourceAccountRepository.class);
        soundcloudConfiguration = mock(SoundcloudConfiguration.class);
        soundcloudService = new Soundcloud(sourceAccountRepository, soundcloudGateway, soundcloudAccessTokenRepository, soundcloudUserRepository, soundcloudConfiguration);
    }

    @Test
    public void testGetAccount() throws SourceAuthenticationException, IOException {
        SourceAccountAuthentication auth = new SourceAccountAuthentication();
        auth.setCode("aCode");

        SoundcloudUser soundcloudUser = mock(SoundcloudUser.class);
        SoundcloudAccessToken soundcloudAccessToken = mock(SoundcloudAccessToken.class);
        when(soundcloudGateway.exchangeToken("aCode")).thenReturn(soundcloudAccessToken);
        when(soundcloudGateway.getMe(soundcloudAccessToken)).thenReturn(soundcloudUser);

        assertThat(soundcloudService.getAccount(auth).getUri(), is(soundcloudUser.getSoundcloudId()));
        assertThat(soundcloudService.getAccount(auth).getSource(), is(SourceType.SOUNDCLOUD));
    }


    @Test(expected = SourceAuthenticationException.class)
    public void testUnsucccessfulGetAccount() throws SourceAuthenticationException, IOException {
        SourceAccountAuthentication auth = new SourceAccountAuthentication();
        auth.setCode("badCode");

        when(soundcloudGateway.exchangeToken("badCode")).thenThrow(new IOException());
        soundcloudService.getAccount(auth);
    }

}
