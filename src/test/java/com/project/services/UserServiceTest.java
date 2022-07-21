package com.project.services;

import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.Role;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.RoleRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.UserService;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.UserConverter;
import com.project.socialnetwork.services.picture_save_services.UserAvatarsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserAvatarsService avatarsOperations;
    @Mock
    private UserConverter userConverter;

    @Test
    public void saveNewUser(){
        String password = "password";
        Role userRole = new Role();
        Set<Role> roles = Collections.singleton(userRole);
        User user = new User();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(passwordEncoder.encode(user.getPassword())).thenReturn(password);
        when(roleRepository.findByName(user.getUsername())).thenReturn(userRole);
        userService.saveNewUser(user);
        verify(userRepository).save(captor.capture());

        String actualPassword = captor.getValue().getPassword();
        Set<Role> actualRoles = captor.getValue().getRoles();

        assertEquals(password,actualPassword);
        assertEquals(roles,actualRoles);
    }

    @Test
    public void updateUserByDTO(){
        User user = new User();
        UserDTO dto = new UserDTO();

        when(userConverter.convertToEntity(dto)).thenReturn(user);
        userService.updateUserByDTO(dto);

        verify(userRepository).save(user);
    }


}