package com.eigenmusik.api.tracks;

import com.eigenmusik.api.sources.Source;
import com.eigenmusik.api.sources.SourceFactory;
import com.eigenmusik.api.user.User;
import com.eigenmusik.api.user.UserDoesntExistException;
import com.eigenmusik.api.user.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/tracks")
@Api(value = "tracks")
public class TracksController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private SourceFactory sourceFactory;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Page<Track> getTracks(Principal principal, Pageable pageable) throws UserDoesntExistException {
        User user = userService.getByUsername(principal.getName());
        user.getUserProfile();
        trackService.createdBy(user.getUserProfile(), pageable);
        return trackService.createdBy(user.getUserProfile(), pageable);
    }

    @RequestMapping(value = "/stream/{trackId}", method = RequestMethod.GET)
    public
    @ResponseBody
    TrackStreamUrl getStreamUrl(@PathVariable Long trackId, Principal principal, Pageable pageable) {
        Track track = trackService.get(trackId);
        Source source = sourceFactory.build(track.getTrackSource().getSource());
        return source.getStreamUrl(track);
    }

}
