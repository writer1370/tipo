package com.tipo.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=87986ccbfb3e07e49593aff62220ee6f&redirect_uri=http://localhost:8080/login-callback&response_type=code&scope=talk_message,friends
    @GetMapping("/login-callback")
    public String getAuthorizeCode(@RequestParam String code){
        System.out.println(code);
        return "";
    }
}
