package com.project.socialnetwork.controllers.authorisation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "/authorisation/login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("errorMessage","inappropriate password and login");
        return "/authorisation/login";
    }
}
