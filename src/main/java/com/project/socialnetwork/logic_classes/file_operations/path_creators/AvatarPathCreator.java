package com.project.socialnetwork.logic_classes.file_operations.path_creators;

import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.FilePathArguments;

import java.text.MessageFormat;

public class AvatarPathCreator extends FilePathCreator<FilePathArguments> {

    public AvatarPathCreator(String resourcesDirectory) {
        super(resourcesDirectory);
    }

    @Override
    protected MessageFormat createMessageFormat() {
        return new MessageFormat("{0}.{1}");
    }

    @Override
    protected Object[] convertPathArgumentsToObjectArray(FilePathArguments pathArguments) {
        return new Object[]{pathArguments.getId(),pathArguments.getFileExtension()};
    }
}
