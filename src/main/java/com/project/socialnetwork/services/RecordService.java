package com.project.socialnetwork.services;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.dtos.RateableRecordDTO;
import com.project.socialnetwork.models.entities.Comment;
import com.project.socialnetwork.models.entities.PostedRecord;
import com.project.socialnetwork.models.entities.RateableRecord;
import com.project.socialnetwork.repositories.RateableRecordRepository;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.RateableRecordConverter;
import com.project.socialnetwork.services.picture_save_services.RecordPictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public record RecordService(RateableRecordRepository recordRepository,
                            RecordPictureService recordPictureOperations,
                            RateableRecordConverter recordConverter,
                            UserService userService) {

    public void saveRecordByDto(RateableRecordDTO dto){
        RateableRecord record = recordConverter.convertToEntity(dto);
        saveRecord(record);
    }

    public void saveRecord(RateableRecord record) {
        record.setUser(userService.receiveCurrentUser());
        record.convertImagesNamesListToJson();
        recordRepository.save(record);
    }

    public void saveComment(Comment comment,Long postedRecordId){
        Optional<PostedRecord> record = findByIdAndType(postedRecordId,PostedRecord.class);
        record.ifPresent(value->{
            comment.setPostedRecord(value);
            saveRecord(comment);
        });
    }

    public void saveNewRecordByDTO(RateableRecordDTO dto,MultipartFile[] images){
        RateableRecord record = recordConverter().convertToEntity(dto);
        saveRecord(record);
        if(AuxiliaryMethods.isMultipartFilesArrayNotEmpty(images)){
            List<String> newImagesNames = savePictures(record.getId(), Arrays.stream(images).toList());
            record.getImagesNames().addAll(newImagesNames);
            saveRecord(record);
        }
    }

    public void deleteById(Long id){
        Optional<RateableRecord> record = findById(id);
        record.ifPresent(value->{
            recordPictureOperations.deletePictures(value.getImagesNames());
            recordRepository.delete(value);
        });
    }

    public Optional<RateableRecord> findById(Long id) {
        Optional<RateableRecord> record = recordRepository.findById(id);
        record.ifPresent(RateableRecord::convertJsonToImagesNamesList);
        return record;
    }

    public Optional<RateableRecordDTO> findDtoByIdAndType(Long id,Class<? extends RateableRecord> cl){
        return findByIdAndType(id,cl).map(recordConverter::convertToDTO);
    }

    public <T extends RateableRecord> Optional<T> findByIdAndType(Long id,Class<T> cl){
        Optional<RateableRecord> record = recordRepository.findById(id);
        if(record.isPresent() && cl.isAssignableFrom(record.get().getClass())){
            record.get().convertJsonToImagesNamesList();
            return record.map(value->(T)value);
        } else{
            return Optional.empty();
        }
    }

    public void deleteImageByIndex(Long recordId,int imageIndex) {
        Optional<RateableRecord> record = findById(recordId);
        record.ifPresent(value->{
            String imageName = value.getImagesNames().get(imageIndex);
            value.getImagesNames().remove(imageIndex);
            recordPictureOperations.deletePicture(imageName);
            saveRecord(value);
        });
    }

    public List<String> savePictures(Long recordId, List<MultipartFile> pictures){
        return recordPictureOperations.savePostPictures(recordId,pictures);
    }

    public boolean isCurrentUserOwner(Long recordId){
        Optional<RateableRecord> record = findById(recordId);
        return record.isPresent() && record.get().getUser().getId().equals(userService.receiveCurrentUserId());
    }

}
