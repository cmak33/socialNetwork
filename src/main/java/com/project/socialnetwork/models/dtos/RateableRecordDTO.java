package com.project.socialnetwork.models.dtos;

import com.project.socialnetwork.models.entities.RateableRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RateableRecordDTO {
    private Long id;
    private String text;
    private Date date;
    private List<String> imagesNames;
    private String entityClass;
}
