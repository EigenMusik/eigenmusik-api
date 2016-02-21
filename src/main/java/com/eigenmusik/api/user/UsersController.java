package com.eigenmusik.api.user;

import com.eigenmusik.api.common.ValidationException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

import static com.eigenmusik.api.common.Errors.newError;

@RequestMapping("/users")
@Controller
@Api(value = "users")
public class UsersController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<?> getMe(Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserDoesntExistException e) {
            return new ResponseEntity<>(newError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
        } catch (ValidationException e) {
            return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
