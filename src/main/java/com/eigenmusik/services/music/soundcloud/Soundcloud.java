package com.eigenmusik.services.music.soundcloud;

import com.eigenmusik.domain.Album;
import com.eigenmusik.domain.Artist;
import com.eigenmusik.domain.Track;
import com.eigenmusik.services.music.MusicService;
import com.eigenmusik.services.music.soundcloud.domain.SoundcloudTrack;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timcoulson on 15/12/2015.
 */
public class Soundcloud implements MusicService {

    private static Logger log = Logger.getLogger(Soundcloud.class);

    public Soundcloud() {
    }

    public List<Track> getTracks() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SoundcloudTrack[]> responseEntity = restTemplate.getForEntity("http://api.soundcloud.com/users/7552497/favorites?client_id=f434bba227f3c05662515accf6d287fc", SoundcloudTrack[].class);

        SoundcloudTrack[] tracks =responseEntity.getBody();
        List<SoundcloudTrack> tracksList = Arrays.asList(tracks);

        return tracksList.stream().map(
                t -> new Track(
                        t.getTitle(),
                        new Artist(t.getUser().getUsername()),
                        new Album("An album"),
                        t.getId().toString(),
                        "SOUNDCLOUD",
                        12345678L)
        ).collect(Collectors.toList());
    }
}
