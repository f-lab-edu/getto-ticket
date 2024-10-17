package com.flab.gettoticket;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
    @PostMapping("/test")
    public String test()  {
        return "success";
    }
}
