package com.project.socialnetwork.logic_classes.file_operations;

import lombok.extern.java.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

@Log
public class FileOperations {

    public static boolean trySaveByteArray(byte[] bytes, Path path){
        boolean isSavedCorrectly = true;
        try{
            Files.write(path,bytes);
        }catch (IOException exception){
            log.log(Level.WARNING,"could not save file",exception);
        }
        return isSavedCorrectly;
    }

    public static boolean trySaveMultipartFile(MultipartFile file,Path path){
        boolean isSavedCorrectly = true;
        try{
            Files.write(path,file.getBytes());
        }catch (IOException exception){
            log.log(Level.WARNING,"could not save multipart file",exception);
        }
        return isSavedCorrectly;
    }

    public static void deleteFile(Path path){
        try{
            Files.deleteIfExists(path);
        }catch(IOException exception){
            log.log(Level.WARNING,"could not delete file",exception);
        }
    }
}
