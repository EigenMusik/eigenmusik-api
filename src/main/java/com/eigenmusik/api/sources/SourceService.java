package com.eigenmusik.api.sources;

import com.eigenmusik.api.tracks.TrackService;
import com.eigenmusik.api.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The EigenMusik external source service.
 */
@Service
public class SourceService {

    private final SourceAccountRepository sourceAccountRepository;
    private final SourceFactory sourceFactory;
    private final TrackService trackService;

    @Autowired
    public SourceService(SourceAccountRepository sourceAccountRepository, SourceFactory sourceFactory, TrackService trackService) {
        this.sourceAccountRepository = sourceAccountRepository;
        this.sourceFactory = sourceFactory;
        this.trackService = trackService;
    }

    /**
     * Get the external source accounts associated with the user.
     *
     * @param userProfile
     * @return
     */
    public List<SourceAccount> getAccounts(UserProfile userProfile) {
        return sourceAccountRepository.findByOwner(userProfile);
    }

    /**
     * Add an external account for the given user.
     *
     * @param sourceType
     * @param auth
     * @param userProfile
     * @return
     * @throws SourceAuthenticationException
     */
    public SourceAccount addAccount(SourceType sourceType, SourceAccountAuthentication auth, UserProfile userProfile) throws SourceAuthenticationException {
        Source source = sourceFactory.build(sourceType);

        SourceAccount sourceAccount = source.getAccount(auth);
        sourceAccount.setOwner(userProfile);
        save(sourceAccount);

        syncAccount(sourceAccount);

        return sourceAccount;
    }

    /**
     * Sync the source account with the external source.
     *
     * @param sourceAccount
     */
    public void syncAccount(SourceAccount sourceAccount) {
        trackService.save(sourceFactory.build(sourceAccount.getSource()).getTracks(sourceAccount), sourceAccount.getOwner());
    }

    /**
     * Get a list of available sources.
     *
     * @return
     */
    public List<SourceJson> getSources() {
        return Arrays.asList(SourceType.values())
                .stream()
                .map(sourceTypes -> sourceFactory.build(sourceTypes).getJson())
                .collect(Collectors.toList());
    }

    /**
     * Save a source account.
     *
     * @param sourceAccount
     * @return
     */
    public SourceAccount save(SourceAccount sourceAccount) {
        return sourceAccountRepository.save(sourceAccount);
    }

}