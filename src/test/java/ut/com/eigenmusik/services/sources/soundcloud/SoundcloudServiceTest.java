package ut.com.eigenmusik.services.sources.soundcloud;

import com.eigenmusik.domain.Track;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.sources.soundcloud.*;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudAccessToken;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudTrack;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudUser;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudAccessTokenRepository;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudTrackRepository;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by timcoulson on 30/12/2015.
 */
public class SoundcloudServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();
    private SoundcloudGateway soundcloudGateway;
    private SoundcloudUserRepository soundcloudUserRepository;
    private SoundcloudAccessTokenRepository soundcloudAccessTokenRepository;
    private SoundcloudTrackRepository soundcloudTrackRepository;
    private TrackRepository trackRepository;
    private SoundcloudService soundcloudService;

    @Before
    public void setUp() throws IOException {

        soundcloudGateway = Mockito.mock(SoundcloudGateway.class);
        soundcloudUserRepository = Mockito.mock(SoundcloudUserRepository.class);
        soundcloudAccessTokenRepository = Mockito.mock(SoundcloudAccessTokenRepository.class);
        trackRepository = Mockito.mock(TrackRepository.class);
        soundcloudService = new SoundcloudService(soundcloudGateway, soundcloudAccessTokenRepository, soundcloudTrackRepository, soundcloudUserRepository, trackRepository);

    }

    @Test
    public void testSuccessfulConnect() throws IOException {
        String code = "aCode";
        SoundcloudAccessToken accessToken = factory.manufacturePojo(SoundcloudAccessToken.class);
        SoundcloudUser soundcloudUser = factory.manufacturePojo(SoundcloudUser.class);
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);

        assertNotNull(accessToken);

        when(soundcloudGateway.getMe(accessToken)
        ).thenReturn(soundcloudUser);

        int i = 0;
        List<SoundcloudTrack> soundcloudTracks = new ArrayList<>();
        while (i < 5) {
            soundcloudTracks.add(i, factory.manufacturePojo(SoundcloudTrack.class));
            i++;
        }

        when(soundcloudGateway.getTracks(soundcloudUser)).thenReturn(soundcloudTracks);
        when(soundcloudGateway.exchangeToken(code)).thenReturn(accessToken);

        assert(soundcloudService.connectAccount(code, userProfile));

        verify(soundcloudUserRepository, times(1)).save(soundcloudUser);
        verify(soundcloudAccessTokenRepository, times(1)).save(accessToken);
        verify(trackRepository, times(1)).save(Mockito.anyListOf(Track.class));
    }

    @Test
    public void testUnsuccessfulConnect() throws IOException {
        String code = "aBadCode";
        SoundcloudAccessToken accessToken = factory.manufacturePojo(SoundcloudAccessToken.class);
        UserProfile userProfile = factory.manufacturePojo(UserProfile.class);

        assertNotNull(accessToken);

        when(soundcloudGateway.exchangeToken(code)
        ).thenThrow(HttpClientErrorException.class);

        assertFalse(soundcloudService.connectAccount(code, userProfile));
    }
}
