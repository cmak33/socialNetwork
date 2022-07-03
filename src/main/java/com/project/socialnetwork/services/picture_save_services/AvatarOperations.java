package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.FilePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.AvatarPathCreator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

@Component
public record AvatarOperations(
        AvatarPathCreator pathCreator,
        String defaultAvatarName) {

    public Optional<String> saveAvatar(MultipartFile file, Long userId) {
        String fileName = pathCreator.createFileName(createArguments(file, userId));
        Path path = pathCreator.createPath(fileName);
        if (FileOperations.trySaveMultipartFile(file, path)) {
            return Optional.of(fileName);
        } else {
            return Optional.empty();
        }
    }

    private FilePathArguments createArguments(MultipartFile file, Long userId) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return new FilePathArguments(userId, extension);
    }

}
