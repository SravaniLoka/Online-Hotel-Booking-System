package com.booking.test;

import com.booking.dao.BookingDAO;
import com.booking.dao.RoomDAO;
import com.booking.exception.BookingException;
import com.booking.exception.DatabaseException;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Booking;
import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.model.User;
import com.booking.service.BookingServiceImpl;
import com.booking.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private RoomDAO roomDAO;

    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingDAO, roomDAO);
    }

    // =========================================================
    // Helper method
    // =========================================================

    private Booking createValidBooking() {

        Booking booking = new Booking();

        User user = new User();
        Hotel hotel = new Hotel();
        Room room = new Room();

        room.setRoomId(1L);
        room.setStatus("AVAILABLE");

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckInDate(
                Date.valueOf("2026-10-01")
        );
        booking.setCheckOutDate(
                Date.valueOf("2026-10-05")
        );
        booking.setGuests(2);
        booking.setTotalAmount(
                new BigDecimal("10000.00")
        );

        return booking;
    }

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    @Test
    void createBooking_success() throws SQLException {

        Booking booking = createValidBooking();
        Connection connection = mock(Connection.class);

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            dbConnection
                    .when(DBConnection::getConnection)
                    .thenReturn(connection);

            when(bookingDAO.create(booking, connection))
                    .thenReturn(true);

            when(roomDAO.update(
                    booking.getRoom(),
                    connection
            )).thenReturn(true);

            boolean result =
                    bookingService.createBooking(booking);

            assertTrue(result);

            verify(bookingDAO, times(1))
                    .create(booking, connection);

            verify(roomDAO, times(1))
                    .update(booking.getRoom(), connection);

            dbConnection.verify(
                    () -> DBConnection.beginTransaction(connection),
                    times(1)
            );

            dbConnection.verify(
                    () -> DBConnection.commitTransaction(connection),
                    times(1)
            );

            dbConnection.verify(
                    () -> DBConnection.rollbackTransaction(connection),
                    never()
            );

            verify(connection, times(1)).close();
        }
    }

    @Test
    void createBooking_bookingCreationFailure()
            throws SQLException {

        Booking booking = createValidBooking();
        Connection connection = mock(Connection.class);

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            dbConnection
                    .when(DBConnection::getConnection)
                    .thenReturn(connection);

            when(bookingDAO.create(booking, connection))
                    .thenReturn(false);

            BookingException exception =
                    assertThrows(
                            BookingException.class,
                            () -> bookingService.createBooking(booking)
                    );

            assertEquals(
                    "Booking could not be created",
                    exception.getMessage()
            );

            verify(bookingDAO, times(1))
                    .create(booking, connection);

            verify(roomDAO, never())
                    .update(
                            any(Room.class),
                            any(Connection.class)
                    );

            dbConnection.verify(
                    () -> DBConnection.rollbackTransaction(connection),
                    atLeastOnce()
            );

            dbConnection.verify(
                    () -> DBConnection.commitTransaction(connection),
                    never()
            );
        }
    }

    @Test
    void createBooking_roomUpdateFailure()
            throws SQLException {

        Booking booking = createValidBooking();
        Connection connection = mock(Connection.class);

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            dbConnection
                    .when(DBConnection::getConnection)
                    .thenReturn(connection);

            when(bookingDAO.create(booking, connection))
                    .thenReturn(true);

            when(roomDAO.update(
                    booking.getRoom(),
                    connection
            )).thenReturn(false);

            BookingException exception =
                    assertThrows(
                            BookingException.class,
                            () -> bookingService.createBooking(booking)
                    );

            assertEquals(
                    "Room status could not be updated",
                    exception.getMessage()
            );

            verify(bookingDAO, times(1))
                    .create(booking, connection);

            verify(roomDAO, times(1))
                    .update(booking.getRoom(), connection);

            dbConnection.verify(
                    () -> DBConnection.rollbackTransaction(connection),
                    atLeastOnce()
            );

            dbConnection.verify(
                    () -> DBConnection.commitTransaction(connection),
                    never()
            );
        }
    }

    @Test
    void createBooking_roomUnavailable()
            throws SQLException {

        Booking booking = createValidBooking();

        booking.getRoom().setStatus("BOOKED");

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            BookingException exception =
                    assertThrows(
                            BookingException.class,
                            () -> bookingService.createBooking(booking)
                    );

            assertEquals(
                    "Room is not available for booking",
                    exception.getMessage()
            );

            verifyNoInteractions(bookingDAO, roomDAO);

            dbConnection.verify(
                    () -> DBConnection.getConnection(),
                    never()
            );
        }
    }

    @Test
    void createBooking_sqlException()
            throws SQLException {

        Booking booking = createValidBooking();

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            dbConnection
                    .when(DBConnection::getConnection)
                    .thenThrow(
                            new SQLException("Database connection error")
                    );

            DatabaseException exception =
                    assertThrows(
                            DatabaseException.class,
                            () -> bookingService.createBooking(booking)
                    );

            assertEquals(
                    "Database error while processing booking",
                    exception.getMessage()
            );

            assertNotNull(exception.getCause());
            assertInstanceOf(
                    SQLException.class,
                    exception.getCause()
            );

            verifyNoInteractions(bookingDAO, roomDAO);

            dbConnection.verify(
                    () -> DBConnection.getConnection(),
                    times(1)
            );

            dbConnection.verify(
                    () -> DBConnection.rollbackTransaction(
                            any(Connection.class)
                    ),
                    never()
            );
        }
    }

    @Test
    void createBooking_commitSqlException()
            throws SQLException {

        Booking booking = createValidBooking();
        Connection connection = mock(Connection.class);

        try (MockedStatic<DBConnection> dbConnection =
                     mockStatic(DBConnection.class)) {

            dbConnection
                    .when(DBConnection::getConnection)
                    .thenReturn(connection);

            when(bookingDAO.create(booking, connection))
                    .thenReturn(true);

            when(roomDAO.update(
                    booking.getRoom(),
                    connection
            )).thenReturn(true);

            dbConnection
                    .when(() ->
                            DBConnection.commitTransaction(connection))
                    .thenThrow(
                            new SQLException("Commit failed")
                    );

            DatabaseException exception =
                    assertThrows(
                            DatabaseException.class,
                            () -> bookingService.createBooking(booking)
                    );

            assertEquals(
                    "Database error while processing booking",
                    exception.getMessage()
            );

            assertNotNull(exception.getCause());
            assertInstanceOf(
                    SQLException.class,
                    exception.getCause()
            );

            dbConnection.verify(
                    () -> DBConnection.rollbackTransaction(connection),
                    times(1)
            );

            verify(connection, times(1)).close();
        }
    }

    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    @Test
    void getBookingById_bookingFound() {

        long bookingId = 1L;
        Booking booking = createValidBooking();

        when(bookingDAO.findById(bookingId))
                .thenReturn(booking);

        Booking result =
                bookingService.getBookingById(bookingId);

        assertNotNull(result);
        assertEquals(booking, result);

        verify(bookingDAO, times(1))
                .findById(bookingId);
    }

    @Test
    void getBookingById_bookingNotFound() {

        long bookingId = 999L;

        when(bookingDAO.findById(bookingId))
                .thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> bookingService.getBookingById(bookingId)
                );

        assertEquals(
                "Booking not found with ID: 999",
                exception.getMessage()
        );

        verify(bookingDAO, times(1))
                .findById(bookingId);
    }

    @Test
    void getBookingById_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.getBookingById(0)
                );

        assertEquals(
                "Invalid booking ID",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    // =========================================================
    // GET ALL BOOKINGS
    // =========================================================

    @Test
    void getAllBookings_success() {

        Booking booking1 = createValidBooking();
        Booking booking2 = createValidBooking();

        List<Booking> bookings =
                Arrays.asList(booking1, booking2);

        when(bookingDAO.findAll())
                .thenReturn(bookings);

        List<Booking> result =
                bookingService.getAllBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(bookings, result);

        verify(bookingDAO, times(1))
                .findAll();
    }

    @Test
    void getAllBookings_emptyList() {

        when(bookingDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Booking> result =
                bookingService.getAllBookings();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookingDAO, times(1))
                .findAll();
    }

    // =========================================================
    // UPDATE BOOKING
    // =========================================================

    @Test
    void updateBooking_success() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);

        when(bookingDAO.update(booking))
                .thenReturn(true);

        boolean result =
                bookingService.updateBooking(booking);

        assertTrue(result);

        verify(bookingDAO, times(1))
                .update(booking);
    }

    @Test
    void updateBooking_notFound() {

        Booking booking = createValidBooking();

        booking.setBookingId(999L);

        when(bookingDAO.update(booking))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Booking not found with ID: 999",
                exception.getMessage()
        );

        verify(bookingDAO, times(1))
                .update(booking);
    }

    @Test
    void updateBooking_nullBooking() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(null)
                );

        assertEquals(
                "Booking cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_invalidId() {

        Booking booking = createValidBooking();

        booking.setBookingId(0L);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Invalid booking ID",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_nullDates() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);
        booking.setCheckInDate(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Check-in and check-out dates cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_invalidDates() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);
        booking.setCheckInDate(
                Date.valueOf("2026-10-10")
        );
        booking.setCheckOutDate(
                Date.valueOf("2026-10-05")
        );

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Check-out date must be after check-in date",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_invalidGuests() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);
        booking.setGuests(0);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Number of guests must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_nullAmount() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);
        booking.setTotalAmount(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Total amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void updateBooking_negativeAmount() {

        Booking booking = createValidBooking();

        booking.setBookingId(1L);
        booking.setTotalAmount(
                new BigDecimal("-100.00")
        );

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.updateBooking(booking)
                );

        assertEquals(
                "Total amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    // =========================================================
    // DELETE BOOKING
    // =========================================================

    @Test
    void deleteBooking_success() {

        long bookingId = 1L;

        when(bookingDAO.delete(bookingId))
                .thenReturn(true);

        boolean result =
                bookingService.deleteBooking(bookingId);

        assertTrue(result);

        verify(bookingDAO, times(1))
                .delete(bookingId);
    }

    @Test
    void deleteBooking_notFound() {

        long bookingId = 999L;

        when(bookingDAO.delete(bookingId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> bookingService.deleteBooking(bookingId)
                );

        assertEquals(
                "Booking not found with ID: 999",
                exception.getMessage()
        );

        verify(bookingDAO, times(1))
                .delete(bookingId);
    }

    @Test
    void deleteBooking_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.deleteBooking(0)
                );

        assertEquals(
                "Invalid booking ID",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    // =========================================================
    // CREATE BOOKING VALIDATION TESTS
    // =========================================================

    @Test
    void createBooking_nullBooking() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(null)
                );

        assertEquals(
                "Booking cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_nullUser() {

        Booking booking = createValidBooking();

        booking.setUser(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Booking user cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_nullHotel() {

        Booking booking = createValidBooking();

        booking.setHotel(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Booking hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_nullRoom() {

        Booking booking = createValidBooking();

        booking.setRoom(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Booking room cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_nullDates() {

        Booking booking = createValidBooking();

        booking.setCheckInDate(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Check-in and check-out dates cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_invalidDates() {

        Booking booking = createValidBooking();

        booking.setCheckInDate(
                Date.valueOf("2026-10-10")
        );
        booking.setCheckOutDate(
                Date.valueOf("2026-10-05")
        );

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Check-out date must be after check-in date",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_invalidGuests() {

        Booking booking = createValidBooking();

        booking.setGuests(0);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Number of guests must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_nullAmount() {

        Booking booking = createValidBooking();

        booking.setTotalAmount(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Total amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }

    @Test
    void createBooking_negativeAmount() {

        Booking booking = createValidBooking();

        booking.setTotalAmount(
                new BigDecimal("-100.00")
        );

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> bookingService.createBooking(booking)
                );

        assertEquals(
                "Total amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(bookingDAO, roomDAO);
    }
}