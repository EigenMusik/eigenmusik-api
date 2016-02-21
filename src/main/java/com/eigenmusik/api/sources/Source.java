package com.eigenmusik.api.sources;

import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.tracks.TrackStreamUrl;

import java.util.List;

/**
 * Abstract class representing an external music source.
 */
public abstract class Source {

    /**
     * Get a JSON representation of the external source.
     *
     * @return
     */
    public SourceJson getJson() {
        return new SourceJson(this.getName(), this.getAuthUrl(), this.getType());
    }

    /**
     * Given an EigenMusik track representation, return a streaming URL.
     *
     * @param track
     * @return TrackStreamUrl
     */
    public abstract TrackStreamUrl getStreamUrl(Track track);

    /**
     * Given an authentication object, return a new EigenMusik source account.
     *
     * @param auth
     * @return
     * @throws SourceAuthenticationException
     */
    public abstract SourceAccount getAccount(SourceAccountAuthentication auth) throws SourceAuthenticationException;

    /**
     * Given an EigenMusik source account, return a list of tracks associated with it.
     *
     * @param account
     * @return
     */
    public abstract List<Track> getTracks(SourceAccount account);

    /**
     * A pretty name for the external source.
     *
     * @return
     */
    public abstract String getName();

    /**
     * Return an authorization URL for the external source.
     *
     * @return
     */
    public abstract String getAuthUrl();

    /**
     * Get the typesafe source type.
     *
     * @return
     */
    public abstract SourceType getType();
}
