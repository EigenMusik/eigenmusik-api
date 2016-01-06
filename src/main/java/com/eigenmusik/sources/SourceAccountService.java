package com.eigenmusik.sources;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackStreamUrl;
import com.eigenmusik.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;

@Service
public class SourceAccountService {

    private final SourceAccountRepository sourceAccountRepository;

    @Autowired
    public SourceAccountService(SourceAccountRepository sourceAccountRepository) {
        this.sourceAccountRepository = sourceAccountRepository;
    }

    public List<SourceAccount> getAccounts(UserProfile userProfile) {
        return sourceAccountRepository.findByOwner(userProfile);
    }
}