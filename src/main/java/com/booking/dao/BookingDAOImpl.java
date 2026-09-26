package com.booking.dao;

import com.booking.model.Booking;
import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.model.User;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingDAOImpl.class);

    // SQL queries
    private static final String BOOKING_CREATE_SQL = """
            INSERT INTO booking
            (user_id, hotel_id, room_id, check_in_date,
             check_out_date, guests, total_amount, booking_status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String BOOKING_FIND_BY_ID_SQL = """
            SELECT booking_id, user_id, hotel_id, room_id,
                   check_in_date, check_out_date, guests,
                   total_amount, booking_status
            FROM booking
            WHERE booking_id = ?
            """;

    private static final String BOOKING_FIND_ALL_SQL = """
            SELECT booking_id, user_id, hotel_id, room_id,
                   check_in_date, check_out_date, guests,
                   total_amount, booking_status
            FROM booking
            ORDER BY booking_id
            """;

    private static final String BOOKING_UPDATE_SQL = """
            UPDATE booking
            SET user_id = ?,
                hotel_id = ?,
                room_id = ?,
                check_in_date = ?,
                check_out_date = ?,
                guests = ?,
                total_amount = ?,
                booking_status = ?
            WHERE booking_id = ?
            """;

    private static final String BOOKING_DELETE_SQL =
            "DELETE FROM booking WHERE booking_id = ?";


    // =========================================================
    // NORMAL CREATE
    // =========================================================

    @Override
    public boolean create(Booking booking) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     BOOKING_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            setBookingParameters(statement, booking);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                setGeneratedBookingId(statement, booking);

                logger.info(
                        "create() completed successfully. bookingId={}",
                        booking.getBookingId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating booking",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }


    // =========================================================
    // TRANSACTION-AWARE CREATE
    // =========================================================

    @Override
    public boolean create(
            Booking booking,
            Connection connection) {

        logger.info(
                "create(booking, connection) started"
        );

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_CREATE_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            setBookingParameters(statement, booking);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                setGeneratedBookingId(statement, booking);

                logger.info(
                        "create(booking, connection) completed successfully. bookingId={}",
                        booking.getBookingId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating booking using transaction",
                    e
            );
        }

        logger.info(
                "create(booking, connection) completed with failure"
        );

        return false;
    }


    // =========================================================
    // FIND BY ID
    // =========================================================

    @Override
    public Booking findById(long bookingId) {

        logger.info(
                "findById() started. bookingId={}",
                bookingId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_FIND_BY_ID_SQL)) {

            statement.setLong(1, bookingId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Booking booking =
                            mapResultSetToBooking(resultSet);

                    logger.info(
                            "findById() completed successfully. bookingId={}",
                            bookingId
                    );

                    return booking;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding booking. bookingId={}",
                    bookingId,
                    e
            );
        }

        logger.info(
                "findById() completed. Booking not found. bookingId={}",
                bookingId
        );

        return null;
    }


    // =========================================================
    // FIND ALL
    // =========================================================

    @Override
    public List<Booking> findAll() {

        logger.info("findAll() started");

        List<Booking> bookings =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_FIND_ALL_SQL);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                bookings.add(
                        mapResultSetToBooking(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. bookingsFound={}",
                    bookings.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all bookings",
                    e
            );
        }

        return bookings;
    }


    // =========================================================
    // NORMAL UPDATE
    // =========================================================

    @Override
    public boolean update(Booking booking) {

        logger.info(
                "update() started. bookingId={}",
                booking.getBookingId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_UPDATE_SQL)) {

            setBookingUpdateParameters(
                    statement,
                    booking
            );

            boolean updated =
                    statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. bookingId={}",
                        booking.getBookingId()
                );

            } else {

                logger.info(
                        "update() completed. No booking updated. bookingId={}",
                        booking.getBookingId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating booking. bookingId={}",
                    booking.getBookingId(),
                    e
            );
        }

        return false;
    }


    // =========================================================
    // TRANSACTION-AWARE UPDATE
    // =========================================================

    @Override
    public boolean update(
            Booking booking,
            Connection connection) {

        logger.info(
                "update(booking, connection) started. bookingId={}",
                booking.getBookingId()
        );

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_UPDATE_SQL)) {

            setBookingUpdateParameters(
                    statement,
                    booking
            );

            boolean updated =
                    statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update(booking, connection) completed successfully. bookingId={}",
                        booking.getBookingId()
                );

            } else {

                logger.info(
                        "update(booking, connection) completed. No booking updated. bookingId={}",
                        booking.getBookingId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating booking using transaction. bookingId={}",
                    booking.getBookingId(),
                    e
            );
        }

        return false;
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public boolean delete(long bookingId) {

        logger.info(
                "delete() started. bookingId={}",
                bookingId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             BOOKING_DELETE_SQL)) {

            statement.setLong(1, bookingId);

            boolean deleted =
                    statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. bookingId={}",
                        bookingId
                );

            } else {

                logger.info(
                        "delete() completed. No booking deleted. bookingId={}",
                        bookingId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting booking. bookingId={}",
                    bookingId,
                    e
            );
        }

        return false;
    }


    // =========================================================
    // HELPER: SET CREATE PARAMETERS
    // =========================================================

    private void setBookingParameters(
            PreparedStatement statement,
            Booking booking)
            throws SQLException {

        statement.setLong(
                1,
                booking.getUser().getUserId()
        );

        statement.setLong(
                2,
                booking.getHotel().getHotelId()
        );

        statement.setLong(
                3,
                booking.getRoom().getRoomId()
        );

        statement.setDate(
                4,
                booking.getCheckInDate()
        );

        statement.setDate(
                5,
                booking.getCheckOutDate()
        );

        statement.setInt(
                6,
                booking.getGuests()
        );

        statement.setBigDecimal(
                7,
                booking.getTotalAmount()
        );

        statement.setString(
                8,
                booking.getBookingStatus()
        );
    }


    // =========================================================
    // HELPER: SET UPDATE PARAMETERS
    // =========================================================

    private void setBookingUpdateParameters(
            PreparedStatement statement,
            Booking booking)
            throws SQLException {

        statement.setLong(
                1,
                booking.getUser().getUserId()
        );

        statement.setLong(
                2,
                booking.getHotel().getHotelId()
        );

        statement.setLong(
                3,
                booking.getRoom().getRoomId()
        );

        statement.setDate(
                4,
                booking.getCheckInDate()
        );

        statement.setDate(
                5,
                booking.getCheckOutDate()
        );

        statement.setInt(
                6,
                booking.getGuests()
        );

        statement.setBigDecimal(
                7,
                booking.getTotalAmount()
        );

        statement.setString(
                8,
                booking.getBookingStatus()
        );

        statement.setLong(
                9,
                booking.getBookingId()
        );
    }


    // =========================================================
    // HELPER: GENERATED BOOKING ID
    // =========================================================

    private void setGeneratedBookingId(
            PreparedStatement statement,
            Booking booking)
            throws SQLException {

        try (ResultSet keys =
                     statement.getGeneratedKeys()) {

            if (keys.next()) {

                booking.setBookingId(
                        keys.getLong(1)
                );
            }
        }
    }


    // =========================================================
    // RESULT SET MAPPING
    // =========================================================

    private Booking mapResultSetToBooking(
            ResultSet resultSet)
            throws SQLException {

        logger.info(
                "mapResultSetToBooking() started"
        );

        User user = new User();

        user.setUserId(
                resultSet.getLong("user_id")
        );

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        Room room = new Room();

        room.setRoomId(
                resultSet.getLong("room_id")
        );

        Booking booking = new Booking(
                resultSet.getLong("booking_id"),
                user,
                hotel,
                room,
                resultSet.getDate("check_in_date"),
                resultSet.getDate("check_out_date"),
                resultSet.getInt("guests"),
                resultSet.getBigDecimal("total_amount"),
                resultSet.getString("booking_status")
        );

        logger.info(
                "mapResultSetToBooking() completed successfully. bookingId={}",
                booking.getBookingId()
        );

        return booking;
    }
}