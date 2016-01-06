package com.eigenmusik.tracks;

import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.sources.SourceService;
import com.eigenmusik.sources.SourceServiceFactory;
import com.eigenmusik.user.User;
import com.eigenmusik.user.UserService;
import com.wordnik.swagger.annotations.Api;
import org.apache.log4j.Logger;
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
public class TrackController {

    private static Logger log = Logger.getLogger(TrackController.class);

    @Autowired
    private TrackService trackService;

    @Autowired
    private SourceServiceFactory sourceServiceFactory;

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
        SourceService sourceService = sourceServiceFactory.build(track.getTrackSource().getSource());
        return sourceService.getStreamUrl(track);
    }

}
