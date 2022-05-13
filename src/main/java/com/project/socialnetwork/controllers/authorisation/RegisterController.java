package com.project.socialnetwork.controllers.authorisation;

import com.project.socialnetwork.models.User;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new User());
        return "/authorisation/register";
    }

    @PostMapping("/register")
    public String registerPost(Model model,@Valid User user, BindingResult bindingResult){
        if(!bindingResult.hasErrors()){
           if(userService.isUsernameUnique(user.getUsername())){
               userService.saveNewUser(user);
               return "redirect:/login";
           } else{
               bindingResult.rejectValue("username","username is already taken");
           }
        }
        model.addAttribute("user",user);
        return "/authorisation/register";
    }
}
