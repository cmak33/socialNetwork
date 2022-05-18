package com.project.socialnetwork.logic_classes.file_operations.picture_saving;

import com.project.socialnetwork.logic_classes.file_operations.ResourcesMultipartFileOperations;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

public class PostPicturesOperations extends ResourcesMultipartFileOperations<PostPicturesOperations.FileCreationArgs> {

    public PostPicturesOperations(String resourcesDirectoryPath) {
        super(resourcesDirectoryPath);
    }

    @Override
    protected MessageFormat createFileNameFormat() {
        return new MessageFormat("{0}_{1}.{2}");
    }

    public static class FileCreationArgs extends ResourcesMultipartFileOperations.FileCreationArgs{
        private final String dateTimeStr;

        public FileCreationArgs(Long id, String fileName, MultipartFile file,String dateTimeStr){
            super(id,fileName,file);
            this.dateTimeStr = dateTimeStr;
        }

        public String getDateTimeStr() {
            return dateTimeStr;
        }

        @Override
        protected Object[] toObjectArray() {
            String extension = FilenameUtils.getExtension(getFileName());
            return new Object[]{getId(), extension,getDateTimeStr()};
        }
    }

}
