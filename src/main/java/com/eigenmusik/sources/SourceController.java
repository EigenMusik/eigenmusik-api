package com.eigenmusik.sources;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.exceptions.UserDoesntExistException;
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

@RequestMapping("/sources")
@Controller
@Api(value = "sources")
public class SourceController {

    private static Logger log = Logger.getLogger(SourceController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SourceJson> index() {
        return sourceService.getSources();
    }

    @RequestMapping(value = "/add/{source}", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<HttpStatus> addSource(
            @PathVariable String source,
            @RequestBody String code,
            Principal principal
    ) throws SourceAuthenticationException, UserDoesntExistException {
        UserProfile userProfile = userService.getByUsername(principal.getName()).getUserProfile();

        SourceType sourceType = SourceType.valueOf(source.toUpperCase());
        sourceService.addAccount(sourceType, code, userProfile);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SourceAccount> getAccounts(
            Principal principal
    ) throws SourceAuthenticationException, UserDoesntExistException {
        UserProfile userProfile = userService.getByUsername(principal.getName()).getUserProfile();

        return sourceService.getAccounts(userProfile);
    }
}
