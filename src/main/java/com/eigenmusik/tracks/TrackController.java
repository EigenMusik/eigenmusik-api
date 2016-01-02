package com.eigenmusik.tracks;

import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.sources.SourceService;
import com.eigenmusik.sources.SourceServiceFactory;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileRepository;
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
@RequestMapping("/rest/tracks")
@Api(value = "tracks")
public class TrackController {

    private static Logger log = Logger.getLogger(TrackController.class);

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SourceServiceFactory sourceServiceFactory;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Page<Track> getTracks(Principal principal, Pageable pageable) {
        UserProfile user = userProfileRepository.findByAccount(
                accountRepository.findByName(
                        principal.getName()
                )
        );
        Page<Track> trackPage = trackRepository.createdBy(user, pageable);
        return trackPage;
    }

    @RequestMapping(value = " /stream/{trackId}", method = RequestMethod.GET)
    public
    @ResponseBody
    TrackStreamUrl getStreamUrl(@PathVariable Long trackId, Principal principal, Pageable pageable) {
        Track track = trackRepository.findOne(trackId);
        SourceService sourceService = sourceServiceFactory.build(track.getTrackSource().getSource());
        return sourceService.getStreamUrl(track);
    }

}
