package com.project.socialnetwork.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends RateableRecord{
    @ManyToOne
    @JoinColumn
    private PostedRecord postedRecord;
}
