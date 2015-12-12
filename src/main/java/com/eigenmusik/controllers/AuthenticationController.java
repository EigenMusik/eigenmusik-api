package com.eigenmusik.controllers;

import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.AccountRepository;
import com.eigenmusik.services.UserProfileRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/rest/auth")
@Controller
public class AuthenticationController {

    private static Logger log = Logger.getLogger(AuthenticationController.class);

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/me")
    public
    @ResponseBody
    UserProfile getMe(Principal principal) {
        return userProfileRepository.findByAccount(
                accountRepository.findByName(
                        principal.getName()
                )
        );
    }
}
