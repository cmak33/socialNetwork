package com.project.socialnetwork.logic_classes.file_operations.file_creation_args;

public  class FilePathArguments {
    private final Long id;
    private final String fileExtension;

    public FilePathArguments(Long id, String fileExtension){
        this.id = id;
        this.fileExtension = fileExtension;
    }

    public Long getId() {
        return id;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
