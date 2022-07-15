package com.project.socialnetwork.services.entity_to_view_representation_converters;


import com.project.socialnetwork.models.view_representations.RateableRecordViewRepresentation;
import com.project.socialnetwork.models.entities.RateableRecord;
import com.project.socialnetwork.services.RatingService;
import com.project.socialnetwork.services.RecordService;
import org.modelmapper.ModelMapper;

public class RecordToViewRepresentationConverter {
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
    private final RecordService recordService;

    public RecordToViewRepresentationConverter(ModelMapper modelMapper,RatingService ratingService,RecordService recordService){
        this.modelMapper = modelMapper;
        this.ratingService = ratingService;
        this.recordService = recordService;
    }

    public RateableRecordViewRepresentation convertToViewRepresentation(RateableRecord record){
        RateableRecordViewRepresentation viewRepresentation = modelMapper.map(record,RateableRecordViewRepresentation.class);
        viewRepresentation.setLiked(ratingService.isLiked(record.getId()));
        viewRepresentation.setDisliked(ratingService.isDisliked(record.getId()));
        viewRepresentation.setOwner(recordService.isCurrentUserOwner(record.getId()));
        viewRepresentation.setUserId(record.getUser().getId());
        viewRepresentation.setImagesNames(record.getImagesNames());
        return viewRepresentation;
    }
}
