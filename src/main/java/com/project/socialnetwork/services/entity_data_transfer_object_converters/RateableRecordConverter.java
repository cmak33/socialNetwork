package com.project.socialnetwork.services.entity_data_transfer_object_converters;

import com.project.socialnetwork.models.dtos.RateableRecordDTO;
import com.project.socialnetwork.models.entities.Comment;
import com.project.socialnetwork.models.entities.RateableRecord;
import com.project.socialnetwork.repositories.CommentRepository;
import com.project.socialnetwork.repositories.RateableRecordRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;


public class RateableRecordConverter extends EntityDataTransferObjectConverter<RateableRecord, RateableRecordDTO> {
    private final CommentRepository commentRepository;
    private final RateableRecordRepository recordRepository;

    public RateableRecordConverter(ModelMapper modelMapper, CommentRepository commentRepository,RateableRecordRepository recordRepository) {
        super(modelMapper);
        this.commentRepository = commentRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public RateableRecordDTO convertToDTO(RateableRecord entity) {
        RateableRecordDTO dto = modelMapper.map(entity,RateableRecordDTO.class);
        dto.setEntityClass(entity.getClass().getName());
        dto.setImagesNames(entity.getImagesNames());
        return dto;
    }

    @Override
    public RateableRecord convertToEntity(RateableRecordDTO dto) {
        RateableRecord record;
        try{
            record = (RateableRecord) modelMapper.map(dto,Class.forName(dto.getEntityClass()));
            if(record.getId()!=null) {
                setEntityDate(record);
                setPostedRecordIfComment(record);
            }
        } catch (Exception exception){
            record = new RateableRecord();
        }
        return record;
    }

    private void setEntityDate(RateableRecord record){
        Optional<RateableRecord> oldRecord = recordRepository.findById(record.getId());
        oldRecord.ifPresent(rateableRecord -> record.setDate(rateableRecord.getDate()));
    }

    private void setPostedRecordIfComment(RateableRecord record){
        if (record.getClass().equals(Comment.class)) {
            Optional<Comment> comment = commentRepository.findById(record.getId());
            comment.ifPresent(value -> ((Comment) record).setPostedRecord(value.getPostedRecord()));
        }
    }
}
