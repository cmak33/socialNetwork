package com.project.socialnetwork.services.entity_data_transfer_object_converters;

import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.UserRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class UserConverter extends EntityDataTransferObjectConverter<User, UserDTO> {
    private final UserRepository userRepository;

    public UserConverter(ModelMapper modelMapper,UserRepository userRepository) {
        super(modelMapper);
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO convertToDTO(User entity) {
        return modelMapper.map(entity,UserDTO.class);
    }

    @Override
    public User convertToEntity(UserDTO dto) {
        User newUser = modelMapper.map(dto,User.class);
        Optional<User> oldUser = userRepository.findById(dto.getId());
        oldUser.ifPresent(value-> newUser.setPassword(value.getPassword()));
        return newUser;
    }
}
