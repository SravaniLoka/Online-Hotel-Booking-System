package com.booking.service;

import com.booking.dao.BookingDAO;
import com.booking.dao.BookingDAOImpl;
import com.booking.dao.RoomDAO;
import com.booking.dao.RoomDAOImpl;
import com.booking.model.Booking;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;

    public BookingServiceImpl() {
        this.bookingDAO = new BookingDAOImpl();
        this.roomDAO = new RoomDAOImpl();
    }

    @Override
    public boolean createBooking(Booking booking) {

        logger.info("createBooking() started");

        Connection connection = null;

        try {

            // Get one connection for the entire transaction
            connection = DBConnection.getConnection();

            // Start transaction
            DBConnection.beginTransaction(connection);

            logger.info("Booking transaction started");

            // Step 1: Create booking
            boolean bookingCreated =
                    bookingDAO.create(booking, connection);

            if (!bookingCreated) {

                logger.error(
                        "Booking creation failed. Rolling back transaction"
                );

                DBConnection.rollbackTransaction(connection);

                return false;
            }

            logger.info(
                    "Booking created successfully. bookingId={}",
                    booking.getBookingId()
            );

            // Step 2: Update room status
            booking.getRoom().setStatus("BOOKED");

            boolean roomUpdated =
                    roomDAO.update(
                            booking.getRoom(),
                            connection
                    );

            if (!roomUpdated) {

                logger.error(
                        "Room update failed. Rolling back transaction"
                );

                DBConnection.rollbackTransaction(connection);

                return false;
            }

            logger.info(
                    "Room status updated successfully. roomId={}",
                    booking.getRoom().getRoomId()
            );

            // Step 3: Commit both operations
            DBConnection.commitTransaction(connection);

            logger.info(
                    "Booking transaction committed successfully. bookingId={}",
                    booking.getBookingId()
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error during booking transaction. Rolling back",
                    e
            );

            if (connection != null) {
                DBConnection.rollbackTransaction(connection);
            }

            return false;

        } finally {

            // Close the transaction connection
            if (connection != null) {

                try {

                    connection.close();

                    logger.info(
                            "Booking transaction connection closed"
                    );

                } catch (SQLException e) {

                    logger.error(
                            "Error while closing transaction connection",
                            e
                    );
                }
            }
        }
    }

    @Override
    public Booking getBookingById(long bookingId) {

        logger.info(
                "getBookingById() started. bookingId={}",
                bookingId
        );

        Booking booking = bookingDAO.findById(bookingId);

        if (booking != null) {
            logger.info(
                    "getBookingById() completed successfully. bookingId={}",
                    bookingId
            );
        } else {
            logger.info(
                    "getBookingById() completed. Booking not found. bookingId={}",
                    bookingId
            );
        }

        return booking;
    }

    @Override
    public List<Booking> getAllBookings() {

        logger.info("getAllBookings() started");

        List<Booking> bookings = bookingDAO.findAll();

        logger.info(
                "getAllBookings() completed successfully. bookingsFound={}",
                bookings.size()
        );

        return bookings;
    }

    @Override
    public boolean updateBooking(Booking booking) {

        logger.info(
                "updateBooking() started. bookingId={}",
                booking.getBookingId()
        );

        boolean updated = bookingDAO.update(booking);

        if (updated) {
            logger.info(
                    "updateBooking() completed successfully. bookingId={}",
                    booking.getBookingId()
            );
        } else {
            logger.info(
                    "updateBooking() failed. bookingId={}",
                    booking.getBookingId()
            );
        }

        return updated;
    }

    @Override
    public boolean deleteBooking(long bookingId) {

        logger.info(
                "deleteBooking() started. bookingId={}",
                bookingId
        );

        boolean deleted = bookingDAO.delete(bookingId);

        if (deleted) {
            logger.info(
                    "deleteBooking() completed successfully. bookingId={}",
                    bookingId
            );
        } else {
            logger.info(
                    "deleteBooking() failed. bookingId={}",
                    bookingId
            );
        }

        return deleted;
    }
}