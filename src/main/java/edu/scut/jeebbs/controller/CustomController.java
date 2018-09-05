package edu.scut.jeebbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class CustomController {

    @RequestMapping(value = "/custom", method = GET)
    public String custom() {
        return "custom";
    }
}
