package com.project.user.service.services.impl;

import com.project.user.service.entities.User;
import com.project.user.service.exceptions.ResourceNotFoundException;
import com.project.user.service.repositories.UserRepository;
import com.project.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server " + id));
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
