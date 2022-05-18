package com.example.springapplication.logic_classes.file_operations.picture_saving;

import com.example.springapplication.logic_classes.file_operations.ResourcesMultipartFileOperations;
import org.apache.commons.io.FilenameUtils;

import java.text.MessageFormat;

public class UserAvatarsOperations extends ResourcesMultipartFileOperations {

    public UserAvatarsOperations(String resourcesDirectoryPath) {
        super(resourcesDirectoryPath);
        fileNameFormat = new MessageFormat("{0}.{1}");
    }

    public String createAvatarName(long userId, String avatarFileName){
        String extension = FilenameUtils.getExtension(avatarFileName);
        Object[] formatArgs = {userId,extension};
        return createFileName(formatArgs);
    }




}
