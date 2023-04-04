package com.learning.springboottest.controller;

import com.learning.springboottest.model.User;
import com.learning.springboottest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
       return userService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable("id") long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long userId,
                                                   @RequestBody User user){
        return new ResponseEntity<>(userService.updateUser(userId,user), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long userId){
        userService.deleteUser(userId);

        return new ResponseEntity<String>("User deleted successfully!.", HttpStatus.OK);
    }

   /* @PutMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<User> addCourseToUser(
            @PathVariable Long userId,
            @PathVariable Long courseId
    ) {
        return new ResponseEntity<>(userService.enrollUserToCourse(userId,courseId), HttpStatus.OK);
    }*/


}
