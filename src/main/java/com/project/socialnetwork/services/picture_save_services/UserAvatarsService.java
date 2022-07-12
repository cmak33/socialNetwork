package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.FilePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.AvatarPathCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

@Service
public class UserAvatarsService extends PictureSaveService<FilePathArguments,AvatarPathCreator>{
    private final String defaultAvatarName;

    @Autowired
    public UserAvatarsService(AvatarPathCreator pathCreator,String defaultAvatarName) {
        super(pathCreator);
        this.defaultAvatarName = defaultAvatarName;
    }

    public Optional<String> saveAvatar(Long userId, String oldAvatarName, MultipartFile avatar){
        FilePathArguments arguments = createFilePathArguments(userId,avatar);
        Optional<String> avatarName = savePicture(avatar,arguments);
        avatarName.ifPresent(value->{
            if(!value.equals(oldAvatarName)) {
                deleteAvatarIfNotDefault(oldAvatarName);
            }
        });
        return avatarName;
    }

    private FilePathArguments createFilePathArguments(Long userId,MultipartFile avatar){
        String extension = FileOperations.getFileExtension(avatar.getOriginalFilename());
        return new FilePathArguments(userId,extension);
    }


    private void deleteAvatarIfNotDefault(String avatarName){
        if(!avatarName.equals(defaultAvatarName)) {
            Path avatarPath = pathCreator.createPath(avatarName);
            FileOperations.deleteFile(avatarPath);
        }
    }

}
