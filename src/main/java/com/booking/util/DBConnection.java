package com.booking.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Logger logger =
            LoggerFactory.getLogger(DBConnection.class);

    private static final String URL =
            "jdbc:mysql://localhost:3306/hotel_booking_system";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {

        logger.info("getConnection() started");

        try {
            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            logger.info("Database connection established successfully");

            return connection;

        } catch (SQLException e) {

            logger.error("Error while establishing database connection", e);

            throw e;
        }
    }
}