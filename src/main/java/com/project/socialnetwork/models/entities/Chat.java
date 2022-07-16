package com.project.socialnetwork.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "chat",cascade = CascadeType.REMOVE)
    private List<Message> messages;
    @ManyToMany
    private List<User> users;
}
