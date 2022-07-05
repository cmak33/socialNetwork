package com.project.socialnetwork.logic_classes.file_operations.path_creators;

import com.project.socialnetwork.logic_classes.file_operations.file_creation_args.FilePathArguments;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public abstract class FilePathCreator<T extends FilePathArguments> {
    private final MessageFormat fileNameFormat;
    private final String resourcesDirectory;

    public FilePathCreator(String resourcesDirectory){
        this.fileNameFormat = createMessageFormat();
        this.resourcesDirectory = resourcesDirectory;
    }

    protected abstract  MessageFormat createMessageFormat();

    public Path createPath(String fileName){
        String fileDirectoryPath = new ClassPathResource(resourcesDirectory).getPath();
        String filePath = fileDirectoryPath+fileName;
        return Paths.get(filePath);
    }

    public String createFileName(T pathArguments){
        return fileNameFormat.format(convertPathArgumentsToObjectArray(pathArguments));
    }

    protected abstract Object[] convertPathArgumentsToObjectArray(T pathArguments);
}
