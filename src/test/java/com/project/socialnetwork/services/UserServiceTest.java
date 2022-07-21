package com.project.socialnetwork.services;

import com.project.socialnetwork.auxiliaryMethods.TestAuxiliaryMethods;
import com.project.socialnetwork.models.dtos.UserDTO;
import com.project.socialnetwork.models.entities.Chat;
import com.project.socialnetwork.models.entities.Role;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.RoleRepository;
import com.project.socialnetwork.repositories.UserRepository;
import com.project.socialnetwork.services.entity_data_transfer_object_converters.UserConverter;
import com.project.socialnetwork.services.picture_save_services.UserAvatarsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserAvatarsService avatarsOperations;
    @Mock
    private UserConverter userConverter;

    @Test
    public void saveNewUser(){
        String password = "password";
        Role userRole = new Role();
        Set<Role> roles = Collections.singleton(userRole);
        User user = new User();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(passwordEncoder.encode(user.getPassword())).thenReturn(password);
        when(roleRepository.findByName(any())).thenReturn(userRole);
        userService.saveNewUser(user);
        verify(userRepository).save(captor.capture());

        String actualPassword = captor.getValue().getPassword();
        Set<Role> actualRoles = captor.getValue().getRoles();

        assertEquals(password,actualPassword);
        assertEquals(roles,actualRoles);
    }

    @Test
    public void updateUserByDTO(){
        User user = new User();
        UserDTO dto = new UserDTO();

        when(userConverter.convertToEntity(dto)).thenReturn(user);
        userService.updateUserByDTO(dto);

        verify(userRepository).save(user);
    }

    @Test
    public void receiveDTOById_ExistingId_ReturnFoundDTO(){
        Long id = 1L;
        Optional<User> user = Optional.of(new User());
        Optional<UserDTO> expectedDTO = Optional.of(new UserDTO());

        when(userService.findById(id)).thenReturn(user);
        when(userConverter.convertToDTO(user.get())).thenReturn(expectedDTO.get());

        Optional<UserDTO> actualDTO = userService.receiveDTOById(id);

        assertEquals(expectedDTO,actualDTO);
    }

    @Test
    public void receiveDTOById_NonexistentId_ReturnEmptyOptional(){
        Long id = 1L;
        Optional<UserDTO> expectedDTO = Optional.empty();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UserDTO> actualDTO = userService.receiveDTOById(id);

        assertEquals(expectedDTO,actualDTO);
    }

    @Test
    public void receiveCurrentUser(){
        User expected = new User();
        TestAuxiliaryMethods.setLoggedInUserToSprintContext(expected);
        User actual = userService.receiveCurrentUser();

        assertEquals(expected,actual);
    }


    @Test
    public void setNewAvatar(){
        UserDTO dto = new UserDTO();
        String expectedAvatarName = "newName";

        when(avatarsOperations.saveAvatar(dto.getId(),dto.getAvatarName(),null)).thenReturn(Optional.of(expectedAvatarName));
        userService.setNewAvatar(dto, null);

        assertEquals(expectedAvatarName,dto.getAvatarName());
    }

    @Test
    public void setNewAvatar_NewAvatarIsNotSaved_OldAvatarNameRemains(){
        String oldAvatarName = "oldName";
        UserDTO dto = new UserDTO();
        dto.setAvatarName(oldAvatarName);

        when(avatarsOperations.saveAvatar(dto.getId(),dto.getAvatarName(),null)).thenReturn(Optional.empty());
        userService.setNewAvatar(dto,null);

        assertEquals(oldAvatarName,dto.getAvatarName());
    }

    @Test
    public void addFriendToCurrentUser(){
        User currentUser = createUserWithEmptyFriendsSet();
        UserService spyService = createSpyServiceWithCurrentUser(currentUser);
        User friend = createUserWithEmptyFriendsSet();
        friend.setId(1L);

        when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
        spyService.addFriendToCurrentUser(friend.getId());

        assertEquals(1, currentUser.getFriends().size());
        assertTrue(currentUser.getFriends().contains(friend));
        assertEquals(1,friend.getFriends().size());
        assertTrue(friend.getFriends().contains(currentUser));
    }

    @Test
    public void removeCurrentUserFriend_FriendIsInFriendSet_RemoveFriend(){
        User currentUser = new User();
        User friend = new User();
        currentUser.setFriends(new HashSet<>(){{add(friend);}});
        friend.setFriends(new HashSet<>(){{add(currentUser);}});
        UserService spyService = createSpyServiceWithCurrentUser(currentUser);

        when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
        spyService.removeCurrentUserFriend(friend.getId());

        assertTrue(currentUser.getFriends().isEmpty());
        assertTrue(friend.getFriends().isEmpty());
    }

    @Test
    public void removeCurrentUserFriend_FriendIsNotInFriendSet_DoNotChangeFriendSet(){
        Set<User> oldFriendsSet = new HashSet<>();
        oldFriendsSet.add(new User());
        User currentUser = new User();
        currentUser.setFriends(new HashSet<>(oldFriendsSet));
        UserService spyService = createSpyServiceWithCurrentUser(currentUser);
        User notFriend = createUserWithEmptyFriendsSet();

        when(userRepository.findById(notFriend.getId())).thenReturn(Optional.of(notFriend));
        spyService.removeCurrentUserFriend(notFriend.getId());

        assertEquals(oldFriendsSet,currentUser.getFriends());
    }

    private UserService createSpyServiceWithCurrentUser(User currentUser){
        UserService spyService = spy(userService);
        doReturn(currentUser).when(spyService).receiveCurrentUser();
        return spyService;
    }

    private User createUserWithEmptyFriendsSet(){
        User user = new User();
        user.setFriends(new HashSet<>());
        return user;
    }

    @Test
    public void findCommonChat(){
        User user = new User();
        user.setId(1L);
        User userInChat = new User();
        userInChat.setId(2L);
        Chat commonChat = createChatWithTwoUsers(user,userInChat);
        user.setChats(Collections.singleton(commonChat));

        Optional<Chat> expected = Optional.of(commonChat);
        Optional<Chat> actual = userService.findCommonChat(user,userInChat.getId());

        assertEquals(expected,actual);
    }

    private Chat createChatWithTwoUsers(User user1,User user2){
        Chat chat = new Chat();
        chat.setUsers(Arrays.asList(user1,user2));
        return chat;
    }

    @Test
    public void findCommonChat_UserWithoutChats_ReturnEmpty(){
        User user = new User();
        user.setId(1L);
        user.setChats(new HashSet<>());
        Long chatInterlocutorId = 2L;

        Optional<Chat> expected = Optional.empty();
        Optional<Chat> actual = userService.findCommonChat(user,chatInterlocutorId);

        assertEquals(expected,actual);
    }



}