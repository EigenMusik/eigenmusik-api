package ut.com.eigenmusik.sources.soundcloud;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.sources.Source;
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
    private SoundcloudService soundcloudService;
    private SourceAccountRepository sourceAccountRepository;

    @Before
    public void setUp() throws IOException {

        soundcloudGateway = mock(SoundcloudGateway.class);
        soundcloudUserRepository = mock(SoundcloudUserRepository.class);
        soundcloudAccessTokenRepository = mock(SoundcloudAccessTokenRepository.class);
        sourceAccountRepository = mock(SourceAccountRepository.class);
        soundcloudService = new SoundcloudService(sourceAccountRepository, soundcloudGateway, soundcloudAccessTokenRepository, soundcloudUserRepository);

    }

    @Test
    public void testGetAccount() throws SourceAuthenticationException, IOException {
        String authCode = "aCode";
        SoundcloudUser soundcloudUser = mock(SoundcloudUser.class);
        SoundcloudAccessToken soundcloudAccessToken = mock(SoundcloudAccessToken.class);
        when(soundcloudGateway.exchangeToken(authCode)).thenReturn(soundcloudAccessToken);
        when(soundcloudGateway.getMe(soundcloudAccessToken)).thenReturn(soundcloudUser);

        assertThat(soundcloudService.getAccount(authCode).getUri(), is(soundcloudUser.getSoundcloudId()));
        assertThat(soundcloudService.getAccount(authCode).getSource(), is(Source.SOUNDCLOUD));
    }


    @Test(expected = SourceAuthenticationException.class)
    public void testUnsucccessfulGetAccount() throws SourceAuthenticationException, IOException {
        String authCode = "badCode";
        when(soundcloudGateway.exchangeToken(authCode)).thenThrow(new IOException());
        soundcloudService.getAccount(authCode);
    }

}
