package com.booking.dao;

import com.booking.model.User;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    // SQL queries
    private static final String USER_CREATE_SQL = """
            INSERT INTO user
            (full_name, email, password_hash, phone, role, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String USER_FIND_BY_ID_SQL = """
            SELECT user_id, full_name, email, password_hash,
                   phone, role, status, created_at, updated_at
            FROM user
            WHERE user_id = ?
            """;

    private static final String USER_FIND_ALL_SQL = """
            SELECT user_id, full_name, email, password_hash,
                   phone, role, status, created_at, updated_at
            FROM user
            ORDER BY user_id
            """;

    private static final String USER_UPDATE_SQL = """
            UPDATE user
            SET full_name = ?,
                email = ?,
                password_hash = ?,
                phone = ?,
                role = ?,
                status = ?
            WHERE user_id = ?
            """;

    private static final String USER_DELETE_SQL =
            "DELETE FROM user WHERE user_id = ?";

    @Override
    public boolean create(User user) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     USER_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        user.setUserId(keys.getLong(1));
                    }
                }

                logger.info(
                        "create() completed successfully. userId={}",
                        user.getUserId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating user",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public User findById(Long userId) {

        logger.info(
                "findById() started. userId={}",
                userId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(USER_FIND_BY_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    User user = mapResultSetToUser(resultSet);

                    logger.info(
                            "findById() completed successfully. userId={}",
                            userId
                    );

                    return user;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding user. userId={}",
                    userId,
                    e
            );
        }

        logger.info(
                "findById() completed. User not found. userId={}",
                userId
        );

        return null;
    }

    @Override
    public List<User> findAll() {

        logger.info("findAll() started");

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(USER_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }

            logger.info(
                    "findAll() completed successfully. usersFound={}",
                    users.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all users",
                    e
            );
        }

        return users;
    }

    @Override
    public boolean update(User user) {

        logger.info(
                "update() started. userId={}",
                user.getUserId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(USER_UPDATE_SQL)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            statement.setLong(7, user.getUserId());

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. userId={}",
                        user.getUserId()
                );

            } else {

                logger.info(
                        "update() completed. No user updated. userId={}",
                        user.getUserId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating user. userId={}",
                    user.getUserId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(Long userId) {

        logger.info(
                "delete() started. userId={}",
                userId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(USER_DELETE_SQL)) {

            statement.setLong(1, userId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. userId={}",
                        userId
                );

            } else {

                logger.info(
                        "delete() completed. No user deleted. userId={}",
                        userId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting user. userId={}",
                    userId,
                    e
            );
        }

        return false;
    }

    private User mapResultSetToUser(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToUser() started");

        User user = new User();

        user.setUserId(resultSet.getLong("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setPhone(resultSet.getString("phone"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));

        Timestamp createdTimestamp =
                resultSet.getTimestamp("created_at");

        if (createdTimestamp != null) {
            user.setCreatedAt(
                    createdTimestamp.toLocalDateTime()
            );
        }

        Timestamp updatedTimestamp =
                resultSet.getTimestamp("updated_at");

        if (updatedTimestamp != null) {
            user.setUpdatedAt(
                    updatedTimestamp.toLocalDateTime()
            );
        }

        logger.info(
                "mapResultSetToUser() completed successfully"
        );

        return user;
    }
}