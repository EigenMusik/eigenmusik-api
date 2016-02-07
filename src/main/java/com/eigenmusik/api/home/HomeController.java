package com.eigenmusik.api.home;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import javax.servlet.ServletContext;
import java.io.IOException;

@Controller
public class HomeController {

    private static Logger log = Logger.getLogger(HomeController.class);
    // TODO I don't like having versions hard coded.
    private static final String SWAGGER_UI_RESOURCE_PATH = "webjars/eigenmusik-swagger-ui/2.1.4/index.html";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext ctx;

    @RequestMapping("/")
    public String home() throws IOException {
        return "redirect:" + SWAGGER_UI_RESOURCE_PATH;
    }
}