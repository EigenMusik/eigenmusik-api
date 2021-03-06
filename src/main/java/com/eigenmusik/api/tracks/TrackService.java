package com.eigenmusik.api.tracks;

import com.eigenmusik.api.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private TrackRepository trackRepository;
    private TrackSourceRepository trackSourceRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository, TrackSourceRepository trackSourceRepository) {
        this.trackRepository = trackRepository;
        this.trackSourceRepository = trackSourceRepository;
    }

    /**
     * Save a list of tracks for the given user.
     *
     * @param tracks
     * @param userProfile
     */
    public void save(List<Track> tracks, UserProfile userProfile) {
        tracks.forEach(t -> t.setCreatedBy(userProfile));
        tracks.forEach(t -> trackSourceRepository.save(t.getTrackSource()));
        trackRepository.save(tracks);
    }

    /**
     * Get a track from a given EigenMusik id.
     *
     * @param trackId
     * @return
     */
    public Track get(Long trackId) {
        return trackRepository.findOne(trackId);
    }

    /**
     * Get tracks for the given user.
     *
     * @param userProfile
     * @param pageable
     * @return
     */
    public Page<Track> createdBy(UserProfile userProfile, Pageable pageable) {
        return trackRepository.createdBy(userProfile, pageable);
    }


}
