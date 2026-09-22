package com.booking;

import com.booking.dao.UserDAO;
import com.booking.model.User;

public class TestUserDAO {

    public static void main(String[] args) {

        User user = new User();

        user.setFullName("Test User");
        user.setEmail("testuser@gmail.com");
        user.setPasswordHash("test123");
        user.setPhone("9876543210");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.createUser(user);

        if (result) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("User creation failed!");
        }
    }
}