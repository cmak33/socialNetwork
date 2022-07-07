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
@Table(name = "posts")
@Getter
@Setter
public class PostedRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String text;
    private Date date;
    private String jsonImagesNames;
    @Transient
    private List<String> imagesNames = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @OneToMany(mappedBy = "postedRecord")
    private Set<Like> likes;
    @ManyToOne
    @JoinColumn
    private User user;

    public void convertJsonToImagesNamesList(){
        imagesNames = JsonConverter.convertJsonToObject(jsonImagesNames);
    }

    public void convertImagesNamesListToJson(){
        jsonImagesNames = JsonConverter.convertObjectToJson(imagesNames);
    }
}
