package com.project.socialnetwork.models.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;
    @ManyToMany
    private List<User> users;
}
