package com.project.socialnetwork.controllers.user;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.PostedRecord;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
            model.addAttribute("pageOwner",owner);
            boolean isOwner = userService.isCurrentUserId(id);
            if(!isOwner){
                Optional<Chat> chat= userService.findCommonChat(userService.receiveCurrentUser(),id);
                Long chatId = chat.map(Chat::getId).orElse(null);
                boolean areFriends = userService.areFriendsWithCurrentUser(id);
                model.addAttribute("chatId",chatId);
                model.addAttribute("areFriends",areFriends);
                return "/profile/profile";
            } else{
                return "/profile/my_profile";
            }
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

    @GetMapping("/{id}/chats")
    public String profileChats(@PathVariable Long id,Model model){
        Optional<User> user = userService.findById(id);
        return user
                .filter(value-> userService.isCurrentUserId(value.getId()))
                .map(value->{
                    List<Long> chatsId = value.getChats().stream().map(Chat::getId).toList();
                    model.addAttribute("chatsId",chatsId);
                    return "profile/chat_list";
                 })
                .orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/{id}/add_to_friends")
    public String addToFriends(@PathVariable Long id){
        userService.addFriendToCurrentUser(id);
        return String.format("redirect:/profiles/%d/",id);
    }

    @GetMapping("/{id}/friends")
    public String friendsList(Model model,@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        return user
                .map(value->{
                    List<Long> usersId = value.getFriends().stream().map(User::getId).toList();
                    model.addAttribute("usersId",usersId);
                    return "profile/friend_list";
                })
                .orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/remove_friend/{id}")
    public String removeFriend(@PathVariable Long id, HttpServletRequest servletRequest){
        userService.removeCurrentUserFriend(id);
        return AuxiliaryMethods.createRedirectionToPreviousPage(servletRequest);
    }


}
