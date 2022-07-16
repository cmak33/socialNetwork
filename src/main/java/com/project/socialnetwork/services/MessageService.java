package com.project.socialnetwork.services;

import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.Message;
import com.project.socialnetwork.repositories.ChatRepository;
import com.project.socialnetwork.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record MessageService(MessageRepository messageRepository,
                             ChatRepository chatRepository,
                             UserService userService) {

    public void createMessage(Message message, Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        message.setSender(userService.receiveCurrentUser());
        chat.ifPresent(value -> {
            message.setChat(value);
            messageRepository.save(message);
        });
    }

    public void updateMessage(Message message) {
        messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Optional<Message> findMessageById(Long id) {
        return messageRepository.findById(id);
    }
}
