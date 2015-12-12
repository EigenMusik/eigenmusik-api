package com.eigenmusik.config;

import com.eigenmusik.domain.*;
import com.eigenmusik.services.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
//@Profile("development")
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = Logger.getLogger(DataSeeder.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        List<Account> accounts = new ArrayList<>();
        List<UserProfile> userProfiles = new ArrayList<>();
        List<Track> tracks = new ArrayList<>();
        SecureRandom rnd = new SecureRandom(SecureRandom.getSeed(25));

        if (accountRepository.count() == 0) {
            for (int i = 0; i < 25; i++) {
                Account account = new Account();
                account.setName("user" + i);
                account.setActive(true);
                account.setEmail("user" + i + "@gmail.com");
                account.setPassword("12345" + i);
                accounts.add(account);

                UserProfile profile = new UserProfile(account);
                profile.setDisplayName("User " + i);
                profile.setReputation(rnd.nextInt(2000));
                userProfiles.add(profile);
            }
            accountRepository.save(accounts);
            userProfileRepository.save(userProfiles);
        }

        if (trackRepository.count() == 0) {
            // Add sample dummy SOUNDCLOUD tracks.
            Artist stickyFingers = new Artist("Sticky Fingers");
            artistRepository.save(stickyFingers);
            Artist nao = new Artist("Nao & Jai Paul");
            artistRepository.save(nao);
            Artist mothAndFlame = new Artist("Moth and Flame");
            artistRepository.save(mothAndFlame);

            Album album = new Album("Some Album");
            albumRepository.save(album);
            Album anotherAlbum = new Album("Another Album");
            albumRepository.save(anotherAlbum);
            Album whatAlbum = new Album("What album?");
            albumRepository.save(whatAlbum);

            Track track1 = new Track("How to fly", stickyFingers, album, "109712283", "SOUNDCLOUD", 12345678L);
            track1.setCreatedBy(userProfiles.get(0));
            track1.setCreatedOn(Calendar.getInstance().getTime());
            Track track2 = new Track("So good", nao, anotherAlbum, "154829271", "SOUNDCLOUD", 12345678L);
            track2.setCreatedBy(userProfiles.get(0));
            track2.setCreatedOn(Calendar.getInstance().getTime());
            Track track3 = new Track("Young & Unafraid", mothAndFlame, whatAlbum, "202988984", "SOUNDCLOUD", 12345678L);
            track3.setCreatedBy(userProfiles.get(0));
            track3.setCreatedOn(Calendar.getInstance().getTime());
            tracks.add(track1);
            tracks.add(track2);
            tracks.add(track3);
            trackRepository.save(tracks);
        }
    }
}
