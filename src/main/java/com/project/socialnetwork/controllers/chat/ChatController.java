package com.project.socialnetwork.controllers.chat;

import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.Message;
import com.project.socialnetwork.services.ChatService;
import com.project.socialnetwork.services.MessageService;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/create_with/{id}")
    public String createChat(@PathVariable Long id){
        Chat chat = new Chat();
        chatService.createChatByInterlocutor(chat,id);
        return String.format("redirect:/chat/%d/",chat.getId());
    }

    @GetMapping("/{id}")
    public String showChat(Model model,@PathVariable Long id){
        Optional<Chat> chat = chatService.findChatById(id);
        return chat.filter(chatService::isCurrentUserInChatParticipants).map(value->{
            model.addAttribute("chat",value);
            model.addAttribute("userId",userService.receiveCurrentUserId());
            model.addAttribute("newMessage",new Message());
            return "chat/chat";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/{id}/delete")
    public String deleteChat(@PathVariable Long id){
        chatService.deleteChat(id);
        return "redirect:/profiles/my_profile/";
    }


    @PostMapping("/{chatId}/create_message")
    public String createMessage(@PathVariable Long chatId,@ModelAttribute("newMessage") Message message,BindingResult result){
        if(!result.hasErrors()){
            messageService.createMessage(message,chatId);
        }
        return String.format("redirect:/chat/%d/",chatId);
    }

    @GetMapping("/message/{id}/edit")
    public String editMessage(Model model,@PathVariable("id") Long id){
        Optional<Message> message = messageService.findMessageById(id);
        return message.map(value->{
            model.addAttribute("message",value);
            return "chat/message_edit";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @PostMapping("/{chatId}/message/{id}/edit")
    public String editMessagePost(@PathVariable("chatId") Long chatId,@ModelAttribute("message") @Valid Message message, BindingResult result){
        if(!result.hasErrors()){
            messageService.updateMessage(message);
            return String.format("redirect:/chat/%d/",chatId);
        } else{
          return String.format("redirect:/message/%d/edit",message.getId());
        }
    }

    @PostMapping("/{chatId}/message/{id}/delete")
    public String deleteMessage(@PathVariable("chatId") Long chatId,@PathVariable("id") Long id){
        messageService.deleteMessage(id);
        return String.format("redirect:/chat/%d/",chatId);
    }

}
