package com.project.socialnetwork.logic_classes.file_operations.file_creation_args;

import java.io.File;

public class PostPicturePathArguments extends FilePathArguments {
    private final int pictureIndex;

    public PostPicturePathArguments(Long id, String fileExtension,int pictureIndex) {
        super(id, fileExtension);
        this.pictureIndex = pictureIndex;
    }

    public int getPictureIndex() {
        return pictureIndex;
    }
}
