package com.example.studyspringbootweb.controller;

import com.example.studyspringbootweb.annotation.SocialUser;
import com.example.studyspringbootweb.domain.Users;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/loginComplete")
    public String loginComplete(@SocialUser Users users) {
        return "redirect:/board/list";
    }
}
