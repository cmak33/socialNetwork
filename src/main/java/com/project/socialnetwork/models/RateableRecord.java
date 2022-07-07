package com.project.socialnetwork.models;

import com.project.socialnetwork.logic_classes.json_converter.JsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public class RateableRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String text;
    private Date date;
    private String jsonImagesNames;
    private Long likesCount;
    private Long dislikeCounts;
    @Transient
    private List<String> imagesNames = new ArrayList<>();
    @ManyToOne
    @JoinColumn
    private User user;
    @OneToMany(mappedBy = "ratedRecord")
    private Set<Like> likes;
    @OneToMany(mappedBy = "ratedRecord")
    private Set<Dislike> dislikes;


    public void convertJsonToImagesNamesList(){
        imagesNames = JsonConverter.convertJsonToObject(jsonImagesNames);
    }

    public void convertImagesNamesListToJson(){
        jsonImagesNames = JsonConverter.convertObjectToJson(imagesNames);
    }
}
