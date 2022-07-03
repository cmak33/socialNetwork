package com.project.socialnetwork.logic_classes.file_operations.path_creators;

import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.PostPicturePathArguments;

import java.text.MessageFormat;

public class PostPicturePathCreator extends FilePathCreator<PostPicturePathArguments> {

    public PostPicturePathCreator(String resourcesDirectory) {
        super(resourcesDirectory);
    }

    @Override
    protected MessageFormat createMessageFormat() {
        return new MessageFormat("{0}_{1}.{2}");
    }

    @Override
    protected Object[] convertPathArgumentsToObjectArray(PostPicturePathArguments pathArguments) {
        return new Object[]{pathArguments.getId(),pathArguments.getPictureIndex(),pathArguments.getFileExtension()};
    }
}
