package com.project.socialnetwork.controllers.user;

import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.PostedRecord;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/my_profile")
    public String profile(Model model){
        model.addAttribute("pageOwner",userService.receiveCurrentUserDTO());
        return "/profile/my_profile";
    }

    @GetMapping("/{id}")
    public String profileById(@PathVariable Long id, Model model){
        Optional<UserDTO> pageOwner = userService.receiveDTOById(id);
        return pageOwner.map(owner->{
            model.addAttribute("pageOwner",pageOwner.get());
            boolean isOwner = userService.isCurrentUserId(id);
            return isOwner?"/profile/my_profile":"/profile/profile";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/my_profile")
    public String refactorProfile(Model model, @ModelAttribute("pageOwner") @Valid UserDTO user, BindingResult result, @ModelAttribute("avatar") MultipartFile avatar){
        if(!result.hasErrors() && userService.isNewUsernameAppropriate(user.getUsername())){
                if(!avatar.isEmpty()){
                    userService.setNewAvatar(user,avatar);
                }
                userService.updateUserByDTO(user);
                model.addAttribute("pageOwner",user);
        } else{
            model.addAttribute("pageOwner",userService.receiveCurrentUserDTO());
            model.addAttribute("error","refactor data was inappropriate!");
        }
        return "/profile/my_profile";
    }

    @GetMapping("/{id}/records")
    public String profileRecords(@PathVariable Long id, Model model){
        Optional<User> user = userService.findById(id);
        return user.map(value->{
            List<Long> recordsId = value.getPostedRecords().stream().map(PostedRecord::getId).toList();
            model.addAttribute("recordsId",recordsId);
            model.addAttribute("isOwner",userService.isCurrentUserId(id));
            return "profile/records_list";
        }).orElse("pageNotFound/pageNotFoundView");
    }

}
