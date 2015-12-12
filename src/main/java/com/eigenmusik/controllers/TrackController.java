package com.eigenmusik.controllers;

import com.eigenmusik.domain.Track;
import com.eigenmusik.services.AccountRepository;
import com.eigenmusik.services.TrackRepository;
import com.eigenmusik.services.UserProfileRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/rest/tracks")
public class TrackController {

    private static Logger log = Logger.getLogger(TrackController.class);

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

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

}
