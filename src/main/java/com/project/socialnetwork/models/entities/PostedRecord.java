package com.project.socialnetwork.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class PostedRecord extends RateableRecord{
    @OneToMany(mappedBy = "postedRecord",cascade = CascadeType.REMOVE)
    private List<Comment> comments;
}
