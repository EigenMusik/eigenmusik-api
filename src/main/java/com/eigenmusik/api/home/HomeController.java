package com.eigenmusik.api.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Controller for the API homepage which includes links to Javadoc, Swagger, coverage etc.
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public ModelAndView home() throws IOException {
        return new ModelAndView("home");
    }
}