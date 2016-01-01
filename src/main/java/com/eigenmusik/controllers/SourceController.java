package com.eigenmusik.controllers;

import com.eigenmusik.domain.Track;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.services.UserService;
import com.eigenmusik.services.repository.TrackRepository;
import com.eigenmusik.services.sources.Source;
import com.eigenmusik.services.sources.SourceService;
import com.eigenmusik.services.sources.SourceServiceFactory;
import com.eigenmusik.services.sources.entity.SourceAccount;
import com.eigenmusik.services.sources.repository.SourceAccountRepository;
import com.eigenmusik.services.sources.repository.TrackSourceRepository;
import com.eigenmusik.services.sources.soundcloud.repository.SoundcloudAccessTokenRepository;
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
    private SoundcloudAccessTokenRepository accessTokenRepository;

    @Autowired
    private UserService userService;

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

        // Save sources first.
        tracks.forEach(t -> t.setTrackSource(trackSourceRepository.save(t.getTrackSource())));
        tracks.forEach(t -> t.setCreatedBy(userProfile));

        trackRepository.save(tracks);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
