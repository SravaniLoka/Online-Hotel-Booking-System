package com.booking.service;

import com.booking.model.User;

import java.util.List;

public interface UserService {

    boolean createUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteUser(Long userId);
}