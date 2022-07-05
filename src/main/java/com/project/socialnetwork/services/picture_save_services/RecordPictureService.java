package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.RecordPicturePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.RecordPicturePathCreator;
import com.project.socialnetwork.models.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordPictureService extends PictureSaveService<RecordPicturePathArguments, RecordPicturePathCreator>{

    public RecordPictureService(RecordPicturePathCreator pathCreator) {
        super(pathCreator);
    }

    public List<String> savePostPictures(Post post,List<MultipartFile> pictures){
        List<String> savedPicturesNames = new ArrayList<>();
        pictures.forEach(file->{
            RecordPicturePathArguments arguments = createPathArguments(file,post,savedPicturesNames.size());
            Optional<String> name = savePicture(file,arguments);
            name.ifPresent(savedPicturesNames::add);
                });
        return savedPicturesNames;
    }

    public void deletePostPictures(Post post){
        post.getImagesNames().forEach(this::deletePicture);
    }

    private RecordPicturePathArguments createPathArguments(MultipartFile file, Post post, int index){
        String extension = FileOperations.getFileExtension(file.getOriginalFilename());
        return new RecordPicturePathArguments(post.getId(),extension,index);
    }
}
