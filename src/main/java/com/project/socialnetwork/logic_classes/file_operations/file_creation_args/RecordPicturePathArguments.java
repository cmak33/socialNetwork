package com.project.socialnetwork.logic_classes.file_operations.file_creation_args;

public class RecordPicturePathArguments extends FilePathArguments {
    private final int pictureIndex;

    public RecordPicturePathArguments(Long id, String fileExtension, int pictureIndex) {
        super(id, fileExtension);
        this.pictureIndex = pictureIndex;
    }

    public int getPictureIndex() {
        return pictureIndex;
    }
}
