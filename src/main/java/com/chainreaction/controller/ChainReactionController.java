package com.chainreaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChainReactionController {

    @GetMapping("/home")
    public String home(){
        return "index";
    }
}
