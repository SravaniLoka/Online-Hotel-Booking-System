package com.booking.dao;

import com.booking.model.User;

import java.util.List;

public interface UserDAO {

    boolean create(User user);

    User findById(Long userId);

    List<User> findAll();

    boolean update(User user);

    boolean delete(Long userId);
}