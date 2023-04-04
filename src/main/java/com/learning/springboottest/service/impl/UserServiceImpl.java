package com.learning.springboottest.service.impl;

import com.learning.springboottest.exception.UserBadRequestException;
import com.learning.springboottest.exception.UserNotFoundException;
import com.learning.springboottest.model.User;
import com.learning.springboottest.repository.UserRepository;
import com.learning.springboottest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found" + id));
        return Optional.of(user);
    }

    @Override
    public User saveUser(User user) {

        boolean isValid = isValidUser(user);
        if(!isValid){
            logger.info("invalid--------");
            throw new UserBadRequestException("Invalid User given" + user);
        }
        Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser.isPresent()){
            throw new UserBadRequestException("User already exist with given email:" + user.getEmail());
        }
        return userRepository.save(user);



    }
    @Override
    public User updateUser(long userId,User user) {

        Optional<User> userFromDB = userRepository.findById(userId);

        if (userFromDB.isEmpty()) {
            throw new UserNotFoundException("User with id:" + userId + " does not exist");
        }

        userFromDB.get().setFirstName(user.getFirstName());
        userFromDB.get().setLastName(user.getLastName());
        userFromDB.get().setEmail(user.getEmail());
        if(user.getEnrollments()!= null) {
            userFromDB.get().setEnrollments(user.getEnrollments());
        }
        userFromDB.get().setRewardPoints(user.getRewardPoints());

        logger.info("userFromDB-------" + userFromDB.get());

        return userRepository.save(userFromDB.get());
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id " + id));
        userRepository.deleteById(id);

    }




     boolean isValidUser(User user) {
        if (user.getFirstName().contains("/") || user.getFirstName().contains("#") || user.getFirstName().contains(" ")) {
            logger.info("entering firstname--------");
            return false;

        }
        if (user.getLastName().contains("/") || user.getLastName().contains("#") || user.getLastName().contains(" ")) {
            logger.info("entering lastname--------");
            return false;
        }
        if(user.getEmail().contains(" ") || !user.getEmail().contains("@") || !user.getEmail().contains(".")){
            logger.info("entering email--------");
            return false;
        }
        return true;
    }

    public User updatePoints(Long userId, User user){

        Optional<User> userFromDB = userRepository.findById(userId);

        if (userFromDB.isEmpty()) {
            throw new UserNotFoundException("User with id:" + userId + " does not exist");
        }
        userFromDB.get().setRewardPoints(user.getRewardPoints());
        return userRepository.save(userFromDB.get());
    }

}



