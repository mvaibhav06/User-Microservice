package com.project.user.service.services;

import com.project.user.service.entities.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    List<User> findAll();
    User findById(String id);
    void deleteById(String id);

}
