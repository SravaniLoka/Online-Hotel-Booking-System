package com.booking.service;

import com.booking.dao.UserDAO;
import com.booking.dao.UserDAOImpl;
import com.booking.model.User;
import com.booking.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean createUser(User user) {

        logger.info("createUser() started");

        // Validation
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }

        if (user.getFullName() == null ||
                user.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full name cannot be empty");
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        if (user.getPasswordHash() == null ||
                user.getPasswordHash().trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }

        if (user.getRole() == null ||
                user.getRole().trim().isEmpty()) {
            throw new ValidationException("Role cannot be empty");
        }

        if (user.getStatus() == null ||
                user.getStatus().trim().isEmpty()) {
            throw new ValidationException("Status cannot be empty");
        }

        boolean created = userDAO.create(user);

        if (created) {
            logger.info("createUser() completed successfully. userId={}",
                    user.getUserId());
        } else {
            logger.info("createUser() failed");
        }

        return created;
    }

    @Override
    public User getUserById(Long userId) {

        logger.info("getUserById() started. userId={}", userId);

        User user = userDAO.findById(userId);

        if (user != null) {
            logger.info("getUserById() completed successfully. userId={}",
                    userId);
        } else {
            logger.info("getUserById() completed. User not found. userId={}",
                    userId);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {

        logger.info("getAllUsers() started");

        List<User> users = userDAO.findAll();

        logger.info("getAllUsers() completed successfully. usersFound={}",
                users.size());

        return users;
    }

    @Override
    public boolean updateUser(User user) {

        logger.info("updateUser() started");

        // Validation
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }

        logger.info("updateUser() started. userId={}",
                user.getUserId());

        if (user.getUserId() == null ||
                user.getUserId() <= 0) {
            throw new ValidationException("Invalid user ID");
        }

        if (user.getFullName() == null ||
                user.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full name cannot be empty");
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        boolean updated = userDAO.update(user);

        if (updated) {
            logger.info("updateUser() completed successfully. userId={}",
                    user.getUserId());
        } else {
            logger.info("updateUser() failed. userId={}",
                    user.getUserId());
        }

        return updated;
    }

    @Override
    public boolean deleteUser(Long userId) {

        logger.info("deleteUser() started. userId={}", userId);

        boolean deleted = userDAO.delete(userId);

        if (deleted) {
            logger.info("deleteUser() completed successfully. userId={}",
                    userId);
        } else {
            logger.info("deleteUser() failed. userId={}",
                    userId);
        }

        return deleted;
    }
}