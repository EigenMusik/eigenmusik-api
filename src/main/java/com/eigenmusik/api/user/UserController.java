package com.eigenmusik.api.user;

import com.eigenmusik.api.exceptions.EmailExistsException;
import com.eigenmusik.api.exceptions.UserDoesntExistException;
import com.eigenmusik.api.exceptions.UsernameExistsException;
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

@RequestMapping("/user")
@Controller
@Api(value = "user")
public class UserController {

    private static Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<?> getMe(Principal principal) throws UserDoesntExistException {
        return new ResponseEntity<>(userService.getByUsername(principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
