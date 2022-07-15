package com.project.socialnetwork.configurations;

import com.project.socialnetwork.repositories.CommentRepository;
import com.project.socialnetwork.repositories.RateableRecordRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.RatingService;
import com.project.socialnetwork.services.RecordService;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.RateableRecordConverter;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.UserConverter;
import com.project.socialnetwork.services.entity_to_view_representation_converters.RecordToViewRepresentationConverter;
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

    @Bean
    @Autowired
    public RateableRecordConverter recordConverter(CommentRepository commentRepository, RateableRecordRepository recordRepository){
        return new RateableRecordConverter(modelMapper(),commentRepository,recordRepository);
    }

    @Bean
    @Autowired
    public RecordToViewRepresentationConverter recordToViewRepresentationConverter(RatingService ratingService, RecordService recordService){
        return new RecordToViewRepresentationConverter(modelMapper(),ratingService,recordService);
    }
}
