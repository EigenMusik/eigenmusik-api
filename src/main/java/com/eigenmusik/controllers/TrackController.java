package com.eigenmusik.controllers;

import com.eigenmusik.domain.Track;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TrackController {

    private static Logger log = Logger.getLogger(TrackController.class);


    @RequestMapping("/rest/tracks")
    public @ResponseBody List<Track> index() {
        return null;
    }

}
