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
            // Add sample dummy SOUNDCLOUD tracks.
            Track track1 = new Track("How to fly", "Sticky Fingers", "109712283" ,"SOUNDCLOUD");
            track1.setCreatedBy(userProfiles.get(0));
            track1.setCreatedOn(Calendar.getInstance().getTime());
            Track track2 = new Track("So good", "Nao & Jai Paul", "154829271", "SOUNDCLOUD");
            track2.setCreatedBy(userProfiles.get(0));
            track2.setCreatedOn(Calendar.getInstance().getTime());
            Track track3 = new Track("Young & Unafraid", "Moth and Flame", "202988984", "SOUNDCLOUD");
            track3.setCreatedBy(userProfiles.get(0));
            track3.setCreatedOn(Calendar.getInstance().getTime());
            tracks.add(track1);
            tracks.add(track2);
            tracks.add(track3);
            trackRepository.save(tracks);
        }
    }
}
