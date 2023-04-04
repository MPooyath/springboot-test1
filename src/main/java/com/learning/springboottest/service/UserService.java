package com.learning.springboottest.service;

import com.learning.springboottest.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
   User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(long id);
    User updateUser(long id ,User user);
    void deleteUser(long id);
    User updatePoints(Long userId , User user);


 /*User enrollUserToCourse(long userId, long courseId);

 public int getTotalPoints(long userId);*/


}
