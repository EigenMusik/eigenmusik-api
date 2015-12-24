package com.eigenmusik.controllers;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.services.AccountRepository;
import com.eigenmusik.services.UserProfileRepository;
import com.eigenmusik.services.UserService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/auth")
@Controller
public class AuthenticationController {

    private static Logger log = Logger.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/me")
    public
    @ResponseBody
    UserProfile getMe(Principal principal) {
        return userService.getUserProfile(principal.getName());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<?> register(@RequestBody Account account) {
        try {
            userService.register(account);
            return new  ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameExistsException e) {
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailExistsException e) {
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
