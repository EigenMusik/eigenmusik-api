package com.eigenmusik.sources;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackService;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserService;
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
    private TrackService trackService;

    @Autowired
    private UserService userProfileService;

    @Autowired
    private UserService userService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SourceServiceFactory sourceServiceFactory;

    @RequestMapping(value = "/add/{source}", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<HttpStatus> addSource(
            @PathVariable String source,
            @RequestBody String code,
            Principal principal
    ) throws SourceAuthenticationException, UserDoesntExistException {
        UserProfile userProfile = userService.getByUsername(principal.getName()).getUserProfile();

        SourceService sourceService = sourceServiceFactory.build(Source.valueOf(source.toUpperCase()));

        SourceAccount sourceAccount = sourceService.getAccount(code);
        sourceAccount.setOwner(userProfile);

        sourceAccount = sourceService.save(sourceAccount);

        List<Track> tracks = sourceService.getTracks(sourceAccount);

        trackService.save(tracks, userProfile);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
