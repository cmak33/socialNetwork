package com.project.socialnetwork.services;


import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.Role;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.RoleRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.UserConverter;
import com.project.socialnetwork.services.picture_save_services.UserAvatarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/properties/user.properties")
@PropertySource("classpath:properties/imagesPaths.properties")
public class UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAvatarsService avatarsOperations;
    private final UserConverter userConverter;
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

    public void setNewAvatar(UserDTO user,MultipartFile avatar){
        Optional<String> avatarName = avatarsOperations.saveAvatar(user.getId(),user.getAvatarName(),avatar);
        avatarName.ifPresent(user::setAvatarName);
    }


    public boolean isNewUsernameAppropriate(String username){
        return isUsernameUnique(username) || receiveCurrentUser().getUsername().equals(username);
    }

    public boolean isUsernameUnique(String username){
        return userRepository.findByUsername(username) == null;
    }

    public void updateUserByDTO(UserDTO userDTO){
        User user = userConverter.convertToEntity(userDTO);
        userRepository.save(user);
    }

    public boolean isCurrentUserId(Long id){
        return id.equals(receiveCurrentUserId());
    }

    public Optional<UserDTO> receiveDTOById(Long id){
        return findById(id).map(userConverter::convertToDTO);
    }

    public List<User> findUsers(List<Long> idList){
        return idList.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public UserDTO receiveCurrentUserDTO(){
        User user = receiveCurrentUser();
        return user!=null?userConverter.convertToDTO(user):null;
    }

    public User receiveCurrentUser(){
        Long id = receiveCurrentUserId();
        return findById(id).orElse(null);
    }

    public Long receiveCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((User)principal).getId();
    }

    public Optional<Chat> findCommonChat(User user,Long secondUserId){
        return user.getChats().stream()
                .filter(chat->chat.getUsers().stream().anyMatch(value->value.getId().equals(secondUserId)))
                .findAny();
    }

}
