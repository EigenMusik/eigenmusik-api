package com.eigenmusik.api.sources;

import com.eigenmusik.api.user.UserDoesntExistException;
import com.eigenmusik.api.user.UserProfile;
import com.eigenmusik.api.user.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/sources")
@Controller
@Api
public class SourcesController {

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
            @RequestBody SourceAccountAuthentication auth,
            Principal principal
    ) throws SourceAuthenticationException, UserDoesntExistException {
        UserProfile userProfile = userService.getByUsername(principal.getName()).getUserProfile();

        SourceType sourceType = SourceType.valueOf(source.toUpperCase());
        sourceService.addAccount(sourceType, auth, userProfile);

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
