package com.eigenmusik.controllers.sources;

import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.UserService;
import com.eigenmusik.services.sources.soundcloud.SoundcloudAccessTokenRepository;
import com.eigenmusik.services.sources.soundcloud.SoundcloudService;
import com.wordnik.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/rest/source/soundcloud")
@Controller
@Api(value="soundcloud")
public class SoundcloudController {

    private static Logger log = Logger.getLogger(SoundcloudController.class);

    @Autowired
    private SoundcloudService soundcloudService;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private SoundcloudAccessTokenRepository accessTokenRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<HttpStatus> add(@RequestBody String code, Principal principal) throws UserDoesntExistException {
        // Retrieve the access token from the authorization code.
        UserProfile user = userService.getUserProfile(principal.getName());
        if (soundcloudService.connectAccount(code, user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        };
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
