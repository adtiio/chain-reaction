package com.chainreaction.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

@Controller
public class ChainReactionController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/new-game")
  public ModelAndView newGame() {
    String sessionId = Integer.toString((int)(Math.random()*1e9)) ;
    return new ModelAndView("redirect:/game").addObject("sessionId", sessionId);
  }

  @GetMapping("/game")
  public ModelAndView game(@RequestParam(value = "sessionId", required = false) String sessionId) {
    ModelAndView modelAndView = new ModelAndView("game");
    modelAndView.addObject("sessionId", sessionId);
    return modelAndView;
  }
}
