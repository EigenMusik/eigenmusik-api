package com.eigenmusik.sources;

import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackRepository;
import com.eigenmusik.tracks.TrackSourceRepository;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileService;
import com.wordnik.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/rest/source")
@Controller
@Api(value = "sources")
public class SourceController {

    private static Logger log = Logger.getLogger(SourceController.class);

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserProfileService userService;

    @Autowired
    private SourceAccountRepository sourceAccountRepository;

    @Autowired
    private TrackSourceRepository trackSourceRepository;

    @Autowired
    private SourceServiceFactory sourceServiceFactory;

    @RequestMapping(value = "/add/{source}", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<HttpStatus> addSource(
            @PathVariable String source,
            @RequestBody String code,
            Principal principal
    ) throws UserDoesntExistException {
        UserProfile userProfile = userService.getUserProfile(principal.getName());

        SourceService sourceService = sourceServiceFactory.build(Source.valueOf(source.toUpperCase()));

        SourceAccount sourceAccount = sourceService.getAccount(code);
        sourceAccount.setOwner(userProfile);

        sourceAccount = sourceAccountRepository.save(sourceAccount);

        List<Track> tracks = sourceService.getTracks(sourceAccount);

        tracks.forEach(t -> trackSourceRepository.save(t.getTrackSource()));
        tracks.forEach(t -> t.setCreatedBy(userProfile));

        trackRepository.save(tracks);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
