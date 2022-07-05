package com.project.socialnetwork.logic_classes.file_operations.path_creators;

import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.RecordPicturePathArguments;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class RecordPicturePathCreator extends FilePathCreator<RecordPicturePathArguments> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    public RecordPicturePathCreator(String resourcesDirectory) {
        super(resourcesDirectory);
    }

    @Override
    protected MessageFormat createMessageFormat() {
        return new MessageFormat("{0}_{1}_{2}.{3}");
    }

    @Override
    protected Object[] convertPathArgumentsToObjectArray(RecordPicturePathArguments pathArguments) {
        return new Object[]{createDateString(),pathArguments.getId(),pathArguments.getPictureIndex(),pathArguments.getFileExtension()};
    }

    private String createDateString(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return dateFormat.format(localDateTime);
    }
}
