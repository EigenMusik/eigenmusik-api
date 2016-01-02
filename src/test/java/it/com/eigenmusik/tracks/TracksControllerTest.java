package it.com.eigenmusik.tracks;

import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackService;
import com.eigenmusik.user.UserService;
import it.com.eigenmusik.IntegrationTestsBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

public class TracksControllerTest extends IntegrationTestsBase {

    @Autowired
    UserService userService;

    @Autowired
    TrackService trackService;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<Track> tracks;

    @Before
    public void Setup() {

    }

    @Test
    public void testGetTracks() throws Exception {

    }
}
