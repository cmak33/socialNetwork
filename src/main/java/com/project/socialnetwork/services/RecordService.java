package com.project.socialnetwork.services;

import com.project.socialnetwork.models.PostedRecord;
import com.project.socialnetwork.models.User;
import com.project.socialnetwork.repositories.RecordRepository;
import com.project.socialnetwork.services.picture_save_services.RecordPictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public record RecordService(RecordRepository recordRepository, RecordPictureService recordPictureOperations) {

    public void saveRecord(PostedRecord record) {
        record.convertImagesNamesListToJson();
        recordRepository.save(record);
    }

    public void deleteRecord(PostedRecord record){
        recordRepository.delete(record);
        recordPictureOperations.deletePostPictures(record);
    }

    public Optional<PostedRecord> findById(Long id) {
        Optional<PostedRecord> record = recordRepository.findById(id);
        record.ifPresent(PostedRecord::convertJsonToImagesNamesList);
        return record;
    }

    public void deleteImage(String imageName, PostedRecord record) {
        record.getImagesNames().remove(imageName);
        recordPictureOperations.deletePicture(imageName);
    }

    public void addPictures(PostedRecord record, List<MultipartFile> pictures){
        List<String> pictureNames = recordPictureOperations.savePostPictures(record,pictures);
        record.getImagesNames().addAll(pictureNames);
    }

    public boolean isOwner(PostedRecord postedRecord, User user){
        return postedRecord.getUser().getId().equals(user.getId());
    }

}
