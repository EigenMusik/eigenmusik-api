package com.eigenmusik.api.messages;

import com.eigenmusik.api.sources.SourceService;
import com.eigenmusik.api.user.UserService;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/messages")
@Controller
@Api(value = "messages")
public class MessagesController {

    private static Logger log = Logger.getLogger(MessagesController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/{lang}/{partial}", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> getMessages(@PathVariable String lang, @PathVariable String partial) {
        return messageService.getMessages(lang, partial);
    }
}
