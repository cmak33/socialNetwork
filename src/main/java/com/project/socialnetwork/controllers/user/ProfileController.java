package com.project.socialnetwork.controllers.user;

import com.project.socialnetwork.models.User;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/profiles")
@RequiredArgsConstructor
@SessionAttributes("pageOwner")
public class ProfileController {
    private final UserService userService;

    @ModelAttribute("pageOwner")
    public User pageOwner(){
        return new User();
    }

    @GetMapping("/my_profile")
    public String profile(Model model){
        model.addAttribute("pageOwner",userService.receiveCurrentUser());
        return "/profile/my_profile";
    }

    @GetMapping("/{id}")
    public String profileById(@PathVariable Long id, Model model){
        Optional<User> pageOwner = userService.findById(id);
        return pageOwner.map(owner->{
            model.addAttribute("pageOwner",pageOwner.get());
            boolean isOwner = userService.isCurrentUserId(id);
            return isOwner?"/profile/my_profile":"/profile/profile";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/my_profile")
    public String refactorProfile(Model model,@ModelAttribute("pageOwner") @Valid User user,BindingResult result,@ModelAttribute("avatar") MultipartFile avatar){
        if(!result.hasErrors() && userService.isNewUsernameAppropriate(user.getUsername())){
                if(!avatar.isEmpty()){
                    userService.setNewAvatar(user,avatar);
                }
                userService.updateUser(user);
                model.addAttribute("pageOwner",user);
        } else{
            model.addAttribute("pageOwner",userService.receiveCurrentUser());
            model.addAttribute("error","refactor data was inappropriate!");
        }
        return "/profile/my_profile";
    }

}
