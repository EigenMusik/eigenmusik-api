package com.eigenmusik.api.sources;

import com.eigenmusik.api.exceptions.SourceAuthenticationException;
import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.tracks.TrackStreamUrl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class Source {

    private SourceAccountRepository sourceAccountRepository;

    @Autowired
    public Source(SourceAccountRepository sourceAccountRepository) {
        this.sourceAccountRepository = sourceAccountRepository;
    }

    public abstract TrackStreamUrl getStreamUrl(Track track);

    public abstract SourceAccount getAccount(SourceAccountAuthentication auth) throws SourceAuthenticationException;

    public abstract List<Track> getTracks(SourceAccount account);

    public abstract String getName();

    public abstract String getAuthUrl();

    public abstract SourceType getType();

    public SourceAccount save(SourceAccount sourceAccount) {
        return sourceAccountRepository.save(sourceAccount);
    }

    public SourceJson getJson() {
        return new SourceJson(this.getName(), this.getAuthUrl(), this.getType());
    }

    ;
}
