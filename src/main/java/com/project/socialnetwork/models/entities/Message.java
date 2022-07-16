package com.project.socialnetwork.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String text;
    @CreationTimestamp
    @Column(updatable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(updatable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(updatable = false)
    private Chat chat;
}
