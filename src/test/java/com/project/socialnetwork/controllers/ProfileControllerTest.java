package com.project.socialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socialnetwork.controllers.user.ProfileController;
import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@ContextConfiguration(classes = ProfileController.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void profile() throws Exception{
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("user");

        when(userService.receiveCurrentUserDTO()).thenReturn(userDTO);

        mockMvc.perform(get("/profiles/my_profile").with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pageOwner",hasProperty("id",is(userDTO.getId()))))
                .andExpect(model().attribute("pageOwner",hasProperty("username",is(userDTO.getUsername()))));
    }

    @Test
    public void profileById_CurrentUser() throws Exception{
            UserDTO userDTO = new UserDTO();
            userDTO.setId(1L);

            String url = createUrlToProfileById(userDTO.getId());
            String viewName = "/profile/my_profile";

            when(userService.receiveDTOById(userDTO.getId())).thenReturn(Optional.of(userDTO));
            when(userService.isCurrentUserId(userDTO.getId())).thenReturn(true);

            mockMvc.perform(get(url).with(user(new User())))
                    .andExpect(status().isOk())
                    .andExpect(view().name(viewName))
                    .andExpect(model().attribute("pageOwner",userDTO));
    }

    @Test
    public void profileById_UserNotFound() throws Exception{
        Long userId = 1L;

        String url = createUrlToProfileById(userId);
        String viewName = "pageNotFound/pageNotFoundView";

        when(userService.receiveDTOById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get(url).with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName));
    }

    @Test
    public void profileById_NotCurrentUser() throws Exception{
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        User currentUser = new User();
        Chat commonChat = new Chat();
        commonChat.setId(2L);

        String url = createUrlToProfileById(userDTO.getId());
        String viewName = "/profile/profile";
        boolean areFriends = true;

        when(userService.receiveDTOById(userDTO.getId())).thenReturn(Optional.of(userDTO));
        when(userService.isCurrentUserId(userDTO.getId())).thenReturn(false);
        when(userService.receiveCurrentUser()).thenReturn(currentUser);
        when(userService.findCommonChat(currentUser,userDTO.getId())).thenReturn(Optional.of(commonChat));
        when(userService.areFriendsWithCurrentUser(userDTO.getId())).thenReturn(areFriends);

        mockMvc.perform(get(url).with(user(currentUser)))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName))
                .andExpect(model().attribute("areFriends",areFriends))
                .andExpect(model().attribute("pageOwner",userDTO))
                .andExpect(model().attribute("chatId",commonChat.getId()));
    }

    private String createUrlToProfileById(Long id){
        return String.format("/profiles/%d/",id);
    }

}