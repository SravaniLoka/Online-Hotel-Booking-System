package com.booking.dao;

import com.booking.model.User;

public class UserDAOManualTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAOImpl();

        User user = new User();

        user.setFullName("DAO Test User");
        user.setEmail("daotest123@gmail.com");
        user.setPasswordHash("test123");
        user.setPhone("9999999999");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        // CREATE
        boolean created = userDAO.create(user);

        System.out.println("CREATE: " + created);
        System.out.println("Generated ID: " + user.getUserId());

        // READ
        User found = userDAO.findById(user.getUserId());

        System.out.println(
                "READ: " +
                        (found != null ? found.getFullName() : "Not found")
        );

        // UPDATE
        found.setFullName("Updated DAO Test User");

        boolean updated = userDAO.update(found);

        System.out.println("UPDATE: " + updated);

        // DELETE
        boolean deleted = userDAO.delete(found.getUserId());

        System.out.println("DELETE: " + deleted);
    }
}