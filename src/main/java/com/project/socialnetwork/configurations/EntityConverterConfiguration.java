package com.project.socialnetwork.configurations;

import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.UserConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConverterConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Autowired
    public UserConverter userConverter(UserRepository userRepository){
        return new UserConverter(modelMapper(),userRepository);
    }
}
