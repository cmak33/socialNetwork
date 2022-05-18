package com.example.springapplication.logic_classes.file_operations;

import lombok.extern.java.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Level;

@Log
public abstract class ResourcesMultipartFileOperations extends  ResourcesFileOperations{

    public ResourcesMultipartFileOperations(String resourcesDirectoryPath) {
        super(resourcesDirectoryPath);
    }

    public boolean trySaveFile(MultipartFile file, String fileSaveName){
        boolean isSavedCorrectly;
        try{
            byte[] bytes = file.getBytes();
            isSavedCorrectly = saveByteArray(bytes,fileSaveName);
        }catch(IOException exception){
            isSavedCorrectly = false;
            log.log(Level.WARNING,"could not read bytes from file",exception);
        }
        return isSavedCorrectly;
    }
}
