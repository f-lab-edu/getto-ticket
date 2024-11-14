package com.flab.gettoticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String login() {
        return "/content/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/content/register";
    }
}
