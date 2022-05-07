package com.project.socialnetwork.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String text;
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;
    @OneToMany(mappedBy = "post")
    private Set<Like> likes;
    @ManyToOne
    @JoinColumn
    private User user;
}
