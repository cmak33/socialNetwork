package com.project.socialnetwork.services;


import com.project.socialnetwork.models.PostedRecord;
import com.project.socialnetwork.models.Role;
import com.project.socialnetwork.models.User;
import com.project.socialnetwork.repositories.RoleRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.picture_save_services.UserAvatarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/properties/user.properties")
@PropertySource("classpath:properties/imagesPaths.properties")
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAvatarsService avatarsOperations;
    @Value("${defaultRole}")
    private String defaultRole;
    @Value("${defaultAvatarName}")
    private String defaultAvatarName;

    public void saveNewUser(User user){
        encodeUserPassword(user);
        addDefaultRoleToUser(user);
        if(user.getAvatarName()==null){
            user.setAvatarName(defaultAvatarName);
        }
        userRepository.save(user);
    }
    private void encodeUserPassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void addDefaultRoleToUser(User user){
        Set<Role> userRoles = Collections.singleton(roleRepository.findByName(defaultRole));
        user.setRoles(userRoles);
    }

    public void setNewAvatar(User user,MultipartFile avatar){
        avatarsOperations.setUserAvatar(user,avatar);
    }


    public boolean isNewUsernameAppropriate(String username){
        return isUsernameUnique(username) || receiveCurrentUser().getUsername().equals(username);
    }

    public boolean isUsernameUnique(String username){
        return userRepository.findByUsername(username) == null;
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public boolean isCurrentUserId(Long id){
        return id.equals(receiveCurrentUserId());
    }

    public boolean isCurrentUserOwnerOfRecord(PostedRecord record){
        return isCurrentUserId(record.getUser().getId());
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User receiveCurrentUser(){
        Long id = receiveCurrentUserId();
        return findById(id).orElse(null);
    }

    private Long receiveCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((User)principal).getId();
    }
}
