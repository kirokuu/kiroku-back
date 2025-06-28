package com.example.kiroku;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class testController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }
}
