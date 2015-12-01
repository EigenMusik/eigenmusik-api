package com.eigenmusik.config;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.Track;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.AccountRepository;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.UserProfileRepository;
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
            for (int i = 0; i < 1000; i++) {
                Track track = new Track("TrackName" + i, "TrackArtist", "12345" ,"SOUNDCLOUD");
                track.setCreatedBy(userProfiles.get(0));
                track.setCreatedOn(Calendar.getInstance().getTime());
                tracks.add(track);
            }

            trackRepository.save(tracks);
        }
    }
}
