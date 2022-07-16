package com.project.socialnetwork.services;

import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record ChatService(ChatRepository chatRepository,UserService userService) {

    public void createChatByInterlocutor(Chat chat,Long id){
        List<Long> participantsId = List.of(id,userService.receiveCurrentUserId());
        createChat(chat,participantsId);
    }

    public void createChat(Chat chat,List<Long> participantsId) {
        List<User> participants = userService.findUsers(participantsId);
        if(participants.size()>1) {
            chat.setUsers(participants);
            chatRepository.save(chat);
        }
    }

    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    public Optional<Chat> findChatById(Long id) {
        return chatRepository.findById(id);
    }

    public boolean isCurrentUserInChatParticipants(Chat chat){
        Long currentUserId = userService.receiveCurrentUserId();
        return chat.getUsers().stream().anyMatch(user->user.getId().equals(currentUserId));
    }

}
