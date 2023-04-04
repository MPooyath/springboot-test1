package com.learning.springboottest.service.impl;

import com.learning.springboottest.exception.UserBadRequestException;
import com.learning.springboottest.exception.UserNotFoundException;
import com.learning.springboottest.model.User;
import com.learning.springboottest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    @BeforeEach
    public void setup(){
        //userRepository = Mockito.mock(UserRepository.class);
        //userService = new UserServiceImpl(userRepository);
        user = new User(1L,"Manju","John","manju@gmail.com");
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnUsersList(){
        // given - precondition or setup

        User user1 = new User(1l,"Saj","Harry","saj@gmail.com");

        given(userRepository.findAll()).willReturn(List.of(user,user1));

        // when -  action or the behaviour that we are going test
        List<User> userList = userService.getAllUsers();

        // then - verify the output
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method (negative scenario)")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnEmptyList(){
        // given - precondition or setup

        User user1 = new User(1l,"Saj","Harry","saj@gmail.com");

        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<User> userList = userService.getAllUsers();

        // then - verify the output
        assertThat(userList).isEmpty();
        assertThat(userList.size()).isEqualTo(0);
    }

    // JUnit test for getUserById method
    @DisplayName("JUnit test for getUserById method")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject(){
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        User savedUser = userService.getUserById(user.getId()).get();

        // then
        assertThat(savedUser).isNotNull();

    }

    // JUnit test for getUserById method -InvalidUserId
    @DisplayName("JUnit test for getUserById method -InvalidUserId")
    @Test
    public void givenUserId_whenGetUserById_thenReturnNull(){
        // given
        long userId =1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });


    }
    // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject(){
        // given - precondition or setup

       //given(userService.isValidUser(any())).willReturn(true);
        //when(userService.isValidUser(any())).thenReturn(true);
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        // when -  action or the behaviour that we are going test
        User savedUser = userService.saveUser(user);

        System.out.println(savedUser);
        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

   /* // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method which throws exception")
    @Test
    public void givenInvalidUser_whenSaveUser_thenThrowsException(){
        // given - precondition or setup

       given(userService.isValidUser(user))
                .willReturn(false);

        System.out.println(user);

        // when -  action or the behaviour that we are going test
        assertThrows(UserBadRequestException.class, () -> {
            userService.saveUser(user);
        });

        // then
        verify(userRepository, never()).save(any(User.class));
    }*/
    // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method with exiting Email which throws exception")
    @Test
    public void givenExitingEmail_whenSaveUser_thenThrowsException(){
        // given - precondition or setup

        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));
        System.out.println(user);

        // when -  action or the behaviour that we are going test
        assertThrows(UserBadRequestException.class, () -> {
            userService.saveUser(user);
        });

        // then
        verify(userRepository, never()).save(any(User.class));
    }
    // JUnit test for updateUser method
    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser(){
        // given - precondition or setup
        long userId =1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        user.setEmail("ram@gmail.com");
        user.setFirstName("Ram");

        // when -  action or the behaviour that we are going test
        User updatedUser = userService.updateUser(userId,user);

        // then - verify the output
        assertThat(updatedUser.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedUser.getFirstName()).isEqualTo("Ram");
    }


    // JUnit test for updateUser method
    @DisplayName("JUnit test for updateUser method- Invalid user")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnNull(){
        // given - precondition or setup
        long userId =1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(userId,user);
        });

        // then
        verify(userRepository, never()).save(any(User.class));
    }

    // JUnit test for deleteUser method
    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void givenUserId_whenDeleteUser_thenNothing(){
        // given - precondition or setup
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(userId);

        // when -  action or the behaviour that we are going test
        userService.deleteUser(userId);

        // then - verify the output
        verify(userRepository, times(1)).deleteById(userId);
    }

    // JUnit test for deleteUser method
    @DisplayName("JUnit test for deleteUser method -InvalidId")
    @Test
    public void givenInvalidUserId_whenDeleteUser_thenThrowsException(){
        // given - precondition or setup
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        // then
        verify(userRepository, never()).deleteById(userId);

    }

    // JUnit test for isValidUser method
    @DisplayName("JUnit test for isValidUser method")
    @Test
    public void givenUserObject_whenIsValid_thenReturnsBooleanValue(){
        // given - precondition or setup
        User newUser = new User(1L,"Manju","John","manju@gmail.com");
        User newUser1 = new User(1L,"/Manju","Tony","tony@gmail.com");
        User newUser2 = new User(1L,"lali"," John","lali@gmail.com");
        User newUser3= new User(1L,"lali","John","laligmail.com");

        assertTrue(userService.isValidUser(newUser));
        assertFalse(userService.isValidUser(newUser1));
        assertFalse(userService.isValidUser(newUser2));
        assertFalse(userService.isValidUser(newUser3));


    }
}
