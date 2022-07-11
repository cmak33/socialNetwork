package com.project.socialnetwork.services;


import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.PostedRecord;
import com.project.socialnetwork.models.entities.Role;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.RoleRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.entities_dto_converter.EntityDataTransferObjectConverter;
import com.project.socialnetwork.services.picture_save_services.UserAvatarsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class UserService implements EntityDataTransferObjectConverter<User,UserDTO> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAvatarsService avatarsOperations;
    private final ModelMapper modelMapper;
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
        avatarsOperations.setUserAvatar(user,avatar);
    }


    public boolean isNewUsernameAppropriate(String username){
        return isUsernameUnique(username) || receiveCurrentUser().getUsername().equals(username);
    }

    public boolean isUsernameUnique(String username){
        return userRepository.findByUsername(username) == null;
    }

    public void updateUserByDTO(UserDTO userDTO){
        User user = convertToEntity(userDTO);
        userRepository.save(user);
    }

    public boolean isCurrentUserId(Long id){
        return id.equals(receiveCurrentUserId());
    }

    public boolean isCurrentUserOwnerOfRecord(PostedRecord record){
        return isCurrentUserId(record.getUser().getId());
    }

    public Optional<UserDTO> receiveDTOById(Long id){
        return findById(id).map(this::convertToDTO);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public UserDTO receiveCurrentUserDTO(){
        User user = receiveCurrentUser();
        return user!=null?convertToDTO(user):null;
    }

    public User receiveCurrentUser(){
        Long id = receiveCurrentUserId();
        return findById(id).orElse(null);
    }

    private Long receiveCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((User)principal).getId();
    }

    @Override
    public UserDTO convertToDTO(User entity) {
        return modelMapper.map(entity,UserDTO.class);
    }

    @Override
    public User convertToEntity(UserDTO dto) {
        User newUser = modelMapper.map(dto,User.class);
        Optional<User> oldUser = findById(dto.getId());
        oldUser.ifPresent(value-> newUser.setPassword(value.getPassword()));
        return newUser;
    }
}
