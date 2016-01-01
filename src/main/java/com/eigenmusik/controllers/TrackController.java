package com.eigenmusik.controllers;

import com.eigenmusik.domain.StreamUrl;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.domain.Track;
import com.eigenmusik.services.AccountRepository;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.UserProfileRepository;
import com.eigenmusik.services.sources.StreamService;
import com.wordnik.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/rest/tracks")
@Api(value="tracks")
public class TrackController {

    private static Logger log = Logger.getLogger(TrackController.class);

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StreamService streamService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Page<Track> getTracks(Principal principal, Pageable pageable) {
        return trackRepository.createdBy(
                userProfileRepository.findByAccount(
                        accountRepository.findByName(
                                principal.getName()
                        )
                ),
                pageable
        );
    }

    @RequestMapping(value = " /stream/{trackId}", method = RequestMethod.GET)
    public
    @ResponseBody
    StreamUrl getStreamUrl(@PathVariable Long trackId, Principal principal, Pageable pageable) {
        Track track = trackRepository.findOne(trackId);
        return streamService.getStream(track);

    }

}
