package com.eigenmusik.services.music;

import com.eigenmusik.domain.Track;

import java.util.List;

/**
 * Created by timcoulson on 15/12/2015.
 */
public interface MusicService {

    public List<Track> getTracks();
}
