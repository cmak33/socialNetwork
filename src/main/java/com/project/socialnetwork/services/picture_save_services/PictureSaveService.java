package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.FilePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.FilePathCreator;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

public abstract class PictureSaveService<V extends  FilePathArguments,T extends FilePathCreator<V>> {
    protected final T pathCreator;

    public PictureSaveService(T pathCreator){
        this.pathCreator = pathCreator;
    }

    public Optional<String> savePicture(MultipartFile file, V pathArguments) {
        String fileName = pathCreator.createFileName(pathArguments);
        Path path = pathCreator.createPath(fileName);
        if (FileOperations.trySaveMultipartFile(file, path)) {
            return Optional.of(fileName);
        } else {
            return Optional.empty();
        }
    }

    public void deletePicture(String pictureName){
        Path picturePath = pathCreator.createPath(pictureName);
        FileOperations.deleteFile(picturePath);
    }
}
