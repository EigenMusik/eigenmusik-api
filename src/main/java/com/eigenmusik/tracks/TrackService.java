package com.eigenmusik.tracks;

import com.eigenmusik.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
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

    public void save(List<Track> tracks, UserProfile userProfile) {
        tracks.forEach(t -> t.setCreatedBy(userProfile));
        tracks.forEach(t -> trackSourceRepository.save(t.getTrackSource()));
        trackRepository.save(tracks);
    }

    public Track get(Long trackId) {
        return trackRepository.findOne(trackId);
    }

    public Page<Track> createdBy(UserProfile userProfile, Pageable pageable) {
        return trackRepository.createdBy(userProfile, pageable);
    }


}
