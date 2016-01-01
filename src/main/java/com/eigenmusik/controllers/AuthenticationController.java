package com.eigenmusik.controllers;

import com.eigenmusik.domain.Account;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.services.UserService;
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

@RequestMapping("/auth")
@Controller
@Api(value = "authentication")
public class AuthenticationController {

    private static Logger log = Logger.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<?> getMe(Principal principal) {
        try {
            return new ResponseEntity<>(userService.getUserProfile(principal.getName()), HttpStatus.OK);
        } catch (UserDoesntExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<?> register(@RequestBody Account account) {
        try {
            userService.register(account);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
