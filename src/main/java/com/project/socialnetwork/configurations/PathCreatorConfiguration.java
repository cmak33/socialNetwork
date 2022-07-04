package com.project.socialnetwork.configurations;


import com.project.socialnetwork.logic_classes.file_operations.path_creators.AvatarPathCreator;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.PostPicturePathCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:properties/imagesPaths.properties")
public class PathCreatorConfiguration {
    @Value("${defaultAvatarName}")
    private String defaultAvatarName;
    @Value("${avatarResourcesPath}")
    private String avatarResourcesPath;
    @Value("${postImageResourcesPath}")
    private String postImageResourcesPath;


    @Bean
    public String getDefaultAvatarName(){
        return defaultAvatarName;
    }

    @Bean
    public AvatarPathCreator getAvatarPathCreator(){
        return new AvatarPathCreator(avatarResourcesPath);
    }

    @Bean
    public PostPicturePathCreator getPostPicturePathCreator(){
        return new PostPicturePathCreator(postImageResourcesPath);
    }



}
