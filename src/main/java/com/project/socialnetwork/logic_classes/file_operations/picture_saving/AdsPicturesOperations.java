package com.example.springapplication.logic_classes.file_operations.picture_saving;

import com.example.springapplication.logic_classes.file_operations.ResourcesMultipartFileOperations;
import org.apache.commons.io.FilenameUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdsPicturesOperations extends ResourcesMultipartFileOperations {

    public AdsPicturesOperations(String resourcesDirectoryPath) {
        super(resourcesDirectoryPath);
        fileNameFormat = new MessageFormat("{0}_{1}_{2}.{3}");
    }

    public String createImageName(long adId,int index,String initialFileName){
        String extension = FilenameUtils.getExtension(initialFileName);
        String dateString = createCurrentDateString();
        Object[] formatArgs = new Object[]{adId,index,dateString,extension};
        return createFileName(formatArgs);
    }

    private String createCurrentDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
        return formatter.format(LocalDateTime.now());
    }
}
