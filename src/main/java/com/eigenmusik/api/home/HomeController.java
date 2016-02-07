package com.eigenmusik.api.home;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class HomeController {

    // TODO I don't like having versions hard coded.
    private static final String SWAGGER_UI_RESOURCE_PATH = "webjars/eigenmusik-swagger-ui/2.1.4/index.html";
    private static Logger log = Logger.getLogger(HomeController.class);
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext ctx;

    @RequestMapping("/")
    public ModelAndView home() throws IOException {
        return new ModelAndView("home");
    }
}