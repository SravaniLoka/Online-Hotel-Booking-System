package com.booking.service;

import com.booking.dao.BookingDAO;
import com.booking.dao.BookingDAOImpl;
import com.booking.dao.RoomDAO;
import com.booking.dao.RoomDAOImpl;
import com.booking.exception.BookingException;
import com.booking.exception.DatabaseException;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Booking;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
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

    public BookingServiceImpl(BookingDAO bookingDAO, RoomDAO roomDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
    }

    @Override
    public boolean createBooking(Booking booking) {

        logger.info("createBooking() started");

        // =========================
        // Validation
        // =========================

        if (booking == null) {
            throw new ValidationException("Booking cannot be null");
        }

        if (booking.getUser() == null) {
            throw new ValidationException("Booking user cannot be null");
        }

        if (booking.getHotel() == null) {
            throw new ValidationException("Booking hotel cannot be null");
        }

        if (booking.getRoom() == null) {
            throw new ValidationException("Booking room cannot be null");
        }

        if (booking.getCheckInDate() == null ||
                booking.getCheckOutDate() == null) {
            throw new ValidationException(
                    "Check-in and check-out dates cannot be null"
            );
        }

        if (!booking.getCheckOutDate().after(
                booking.getCheckInDate())) {
            throw new ValidationException(
                    "Check-out date must be after check-in date"
            );
        }

        if (booking.getGuests() <= 0) {
            throw new ValidationException(
                    "Number of guests must be greater than zero"
            );
        }

        if (booking.getTotalAmount() == null ||
                booking.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Total amount cannot be negative"
            );
        }

        // =========================
        // Booking-specific validation
        // =========================

        String roomStatus = booking.getRoom().getStatus();

        if (roomStatus == null ||
                !roomStatus.equalsIgnoreCase("AVAILABLE")) {

            throw new BookingException(
                    "Room is not available for booking"
            );
        }

        Connection connection = null;

        try {

            // =========================
            // Start transaction
            // =========================

            connection = DBConnection.getConnection();

            DBConnection.beginTransaction(connection);

            logger.info("Booking transaction started");

            // =========================
            // Step 1: Create booking
            // =========================

            boolean bookingCreated =
                    bookingDAO.create(booking, connection);

            if (!bookingCreated) {

                logger.error(
                        "Booking creation failed. Rolling back transaction"
                );

                DBConnection.rollbackTransaction(connection);

                throw new BookingException(
                        "Booking could not be created"
                );
            }

            logger.info(
                    "Booking created successfully. bookingId={}",
                    booking.getBookingId()
            );

            // =========================
            // Step 2: Update room status
            // =========================

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

                throw new BookingException(
                        "Room status could not be updated"
                );
            }

            logger.info(
                    "Room status updated successfully. roomId={}",
                    booking.getRoom().getRoomId()
            );

            // =========================
            // Step 3: Commit transaction
            // =========================

            DBConnection.commitTransaction(connection);

            logger.info(
                    "Booking transaction committed successfully. bookingId={}",
                    booking.getBookingId()
            );

            return true;

        } catch (BookingException e) {

            logger.error(
                    "Booking operation failed",
                    e
            );

            if (connection != null) {
                DBConnection.rollbackTransaction(connection);
            }

            throw e;

        } catch (SQLException e) {

            logger.error(
                    "Database error during booking transaction. Rolling back",
                    e
            );

            if (connection != null) {
                DBConnection.rollbackTransaction(connection);
            }

            throw new DatabaseException(
                    "Database error while processing booking",
                    e
            );

        } finally {

            // =========================
            // Close connection
            // =========================

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

        // Validation
        if (bookingId <= 0) {
            throw new ValidationException(
                    "Invalid booking ID"
            );
        }

        Booking booking = bookingDAO.findById(bookingId);

        if (booking == null) {

            logger.info(
                    "getBookingById() completed. Booking not found. bookingId={}",
                    bookingId
            );

            throw new ResourceNotFoundException(
                    "Booking not found with ID: " + bookingId
            );
        }

        logger.info(
                "getBookingById() completed successfully. bookingId={}",
                bookingId
        );

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

        logger.info("updateBooking() started");

        // Validation
        if (booking == null) {
            throw new ValidationException(
                    "Booking cannot be null"
            );
        }

        logger.info(
                "updateBooking() started. bookingId={}",
                booking.getBookingId()
        );

        if (booking.getBookingId() <= 0) {
            throw new ValidationException(
                    "Invalid booking ID"
            );
        }

        if (booking.getCheckInDate() == null ||
                booking.getCheckOutDate() == null) {
            throw new ValidationException(
                    "Check-in and check-out dates cannot be null"
            );
        }

        if (!booking.getCheckOutDate().after(
                booking.getCheckInDate())) {
            throw new ValidationException(
                    "Check-out date must be after check-in date"
            );
        }

        if (booking.getGuests() <= 0) {
            throw new ValidationException(
                    "Number of guests must be greater than zero"
            );
        }

        if (booking.getTotalAmount() == null ||
                booking.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Total amount cannot be negative"
            );
        }

        boolean updated = bookingDAO.update(booking);

        if (!updated) {

            logger.info(
                    "updateBooking() failed. bookingId={}",
                    booking.getBookingId()
            );

            throw new ResourceNotFoundException(
                    "Booking not found with ID: " +
                            booking.getBookingId()
            );
        }

        logger.info(
                "updateBooking() completed successfully. bookingId={}",
                booking.getBookingId()
        );

        return true;
    }

    @Override
    public boolean deleteBooking(long bookingId) {

        logger.info(
                "deleteBooking() started. bookingId={}",
                bookingId
        );

        // Validation
        if (bookingId <= 0) {
            throw new ValidationException(
                    "Invalid booking ID"
            );
        }

        boolean deleted = bookingDAO.delete(bookingId);

        if (!deleted) {

            logger.info(
                    "deleteBooking() failed. bookingId={}",
                    bookingId
            );

            throw new ResourceNotFoundException(
                    "Booking not found with ID: " + bookingId
            );
        }

        logger.info(
                "deleteBooking() completed successfully. bookingId={}",
                bookingId
        );

        return true;
    }
}