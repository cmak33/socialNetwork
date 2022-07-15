package com.project.socialnetwork.services.picture_save_services;

import com.project.socialnetwork.logic_classes.file_operations.FileOperations;
import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.RecordPicturePathArguments;
import com.project.socialnetwork.logic_classes.file_operations.path_creators.RecordPicturePathCreator;
import com.project.socialnetwork.models.entities.RateableRecord;
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

    public List<String> savePostPictures(Long recordId, List<MultipartFile> pictures){
        List<String> savedPicturesNames = new ArrayList<>();
        pictures.forEach(file->{
            RecordPicturePathArguments arguments = createPathArguments(file,recordId,savedPicturesNames.size());
            Optional<String> name = savePicture(file,arguments);
            name.ifPresent(savedPicturesNames::add);
                });
        return savedPicturesNames;
    }

    public void deletePictures(List<String> imagesNames){
        imagesNames.forEach(this::deletePicture);
    }

    private RecordPicturePathArguments createPathArguments(MultipartFile file, Long recordId, int index){
        String extension = FileOperations.getFileExtension(file.getOriginalFilename());
        return new RecordPicturePathArguments(recordId,extension,index);
    }
}
