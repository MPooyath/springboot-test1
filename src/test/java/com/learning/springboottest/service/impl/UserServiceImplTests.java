package com.learning.springboottest.service.impl;

import com.learning.springboottest.exception.UserBadRequestException;
import com.learning.springboottest.exception.UserNotFoundException;
import com.learning.springboottest.model.User;
import com.learning.springboottest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void saveUser() {
        //Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(new User(1L, "John", "Doe", "john.doe@example.com"));
        //Act
        User result = userService.saveUser(user);
        //Assert
        assertNotNull(result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRewardPoints(), result.getRewardPoints());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void getAllUsers() {
        //Arrange
        User user1 = new User(1L, "John", "Doe", "john.doe@example.com");
        User user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        //Act
        List<User> result = userService.getAllUsers();
        //Assert
        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    @Test
    public void getUserById() {
        //Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //Act
        Optional<User> result = userService.getUserById(1L);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void getUserByIdThrowsExceptionWhenUserNotFound() {
        //Act
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }


    @Test
    public void saveUserThrowsExceptionWhenUserInvalid() {

        User user = new User(1L, "John/", "Doe", "john.doe@example.com");
        //Assert
        assertThrows(UserBadRequestException.class, () -> userService.saveUser(user));
    }

    @Test
   public void saveUserThrowsExceptionWhenUserAlreadyExist() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        //Assert
        assertThrows(UserBadRequestException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        long userId = 1L;
        User existingUser = new User(userId, "John", "Doe", "johndoe@example.com", 0);
        User updatedUser = new User(userId, "Jane", "Doe", "janedoe@example.com", 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.updateUser(userId, updatedUser);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
        assertEquals(userId, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("janedoe@example.com", result.getEmail());
        assertEquals(10, result.getRewardPoints());
    }

    @Test
    public void givenUserObject_whenIsValid_thenReturnsBooleanValue() {
        // Arrange
        User newUser = new User(1L, "Manju", "John", "manju@gmail.com");
        User newUser1 = new User(1L, "/Manju", "Tony", "tony@gmail.com");
        User newUser2 = new User(1L, "lali", " John", "lali@gmail.com");
        User newUser3 = new User(1L, "lali", "John", "laligmail.com");

        //Assert
        assertTrue(userService.isValidUser(newUser));
        assertFalse(userService.isValidUser(newUser1));
        assertFalse(userService.isValidUser(newUser2));
        assertFalse(userService.isValidUser(newUser3));


    }
}
