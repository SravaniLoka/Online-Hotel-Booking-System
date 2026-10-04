package com.booking.Controller;

import com.booking.model.User;
import com.booking.service.UserService;
import com.booking.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    public boolean createUser(User user) {

        logger.info("createUser() requested");

        boolean created = userService.createUser(user);

        if (created) {
            logger.info(
                    "createUser() completed successfully. userId={}",
                    user.getUserId()
            );
        } else {
            logger.info("createUser() failed");
        }

        return created;
    }

    public User getUserById(Long userId) {

        logger.info(
                "getUserById() requested. userId={}",
                userId
        );

        User user = userService.getUserById(userId);

        if (user != null) {
            logger.info(
                    "getUserById() completed successfully. userId={}",
                    userId
            );
        } else {
            logger.info(
                    "getUserById() completed. User not found. userId={}",
                    userId
            );
        }

        return user;
    }

    public List<User> getAllUsers() {

        logger.info("getAllUsers() requested");

        List<User> users = userService.getAllUsers();

        logger.info(
                "getAllUsers() completed successfully. usersFound={}",
                users.size()
        );

        return users;
    }

    public boolean updateUser(User user) {

        logger.info(
                "updateUser() requested. userId={}",
                user.getUserId()
        );

        boolean updated = userService.updateUser(user);

        if (updated) {
            logger.info(
                    "updateUser() completed successfully. userId={}",
                    user.getUserId()
            );
        } else {
            logger.info(
                    "updateUser() failed. userId={}",
                    user.getUserId()
            );
        }

        return updated;
    }

    public boolean deleteUser(Long userId) {

        logger.info(
                "deleteUser() requested. userId={}",
                userId
        );

        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            logger.info(
                    "deleteUser() completed successfully. userId={}",
                    userId
            );
        } else {
            logger.info(
                    "deleteUser() failed. userId={}",
                    userId
            );
        }

        return deleted;
    }
}