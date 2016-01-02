package ut.com.eigenmusik.sources.soundcloud;

import com.eigenmusik.sources.soundcloud.SoundcloudService;
import com.eigenmusik.sources.soundcloud.SoundcloudGateway;
import com.eigenmusik.sources.soundcloud.SoundcloudAccessTokenRepository;
import com.eigenmusik.sources.soundcloud.SoundcloudUserRepository;
import org.junit.Before;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

public class SoundcloudServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudUserRepository soundcloudUserRepository;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private SoundcloudService soundcloudService;

    @Before
    public void setUp() throws IOException {

        soundcloudGateway = Mockito.mock(SoundcloudGateway.class);
        soundcloudUserRepository = Mockito.mock(SoundcloudUserRepository.class);
        soundcloudAccessTokenRepository = Mockito.mock(SoundcloudAccessTokenRepository.class);
        soundcloudService = new SoundcloudService(soundcloudGateway, soundcloudAccessTokenRepository, soundcloudUserRepository);

    }

}
