package com.example.springapplication.logic_classes.file_operations;

import org.springframework.core.io.ClassPathResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public abstract class ResourcesFileOperations{
    private final String resourcesDirectoryPath;
    protected MessageFormat fileNameFormat;

    public ResourcesFileOperations(String resourcesDirectoryPath){
        this.resourcesDirectoryPath = resourcesDirectoryPath;
    }

    public boolean saveByteArray(byte[] bytes,String fileName){
        Path filePath = createFilePath(fileName);
        return FileOperations.trySaveByteArray(bytes,filePath);
    }

    public void deleteFile(String fileName){
        Path filePath = createFilePath(fileName);
        FileOperations.deleteFile(filePath);
    }


    private Path createFilePath(String fileName){
        String strPath = receiveDirectoryAbsolutePath()+fileName;
        return Paths.get(strPath);
    }

    private String receiveDirectoryAbsolutePath(){
        return  new ClassPathResource(resourcesDirectoryPath).getPath();
    }

    protected String createFileName(Object[] formatArgs){
        return fileNameFormat.format(formatArgs);
    }

}
