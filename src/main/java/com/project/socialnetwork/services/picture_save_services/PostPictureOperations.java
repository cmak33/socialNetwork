package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.PostPicturePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.PostPicturePathCreator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

public class PostPictureOperations {
    private final PostPicturePathCreator pathCreator;

    public PostPictureOperations(PostPicturePathCreator pathCreator){
        this.pathCreator = pathCreator;
    }

    public Optional<String> savePostPicture(MultipartFile file, Long postId, int pictureIndex){
        String fileName = pathCreator.createFileName(createPathArguments(file, postId,pictureIndex));
        Path path = pathCreator.createPath(fileName);
        if (FileOperations.trySaveMultipartFile(file, path)) {
            return Optional.of(fileName);
        } else {
            return Optional.empty();
        }
    }

    private PostPicturePathArguments createPathArguments(MultipartFile file,Long postId,int pictureIndex){
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return new PostPicturePathArguments(postId,extension,pictureIndex);
    }
}
