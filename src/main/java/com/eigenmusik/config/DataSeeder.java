package com.eigenmusik.config;

import com.eigenmusik.domain.*;
import com.eigenmusik.services.*;
import com.eigenmusik.services.music.soundcloud.Soundcloud;
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
            Soundcloud sc = new Soundcloud();
            List<Track> tracks = sc.getTracks();
            tracks.forEach(track -> track.setCreatedBy(userProfiles.get(0)));
            tracks.forEach(track1 -> track1.setCreatedOn(Calendar.getInstance().getTime()));
            trackRepository.save(tracks);

            for(Track track : tracks) {
                logger.info(track);
            }

        }
    }
}
