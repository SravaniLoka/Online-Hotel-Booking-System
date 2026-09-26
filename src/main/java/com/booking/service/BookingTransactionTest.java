package com.booking.service;

import com.booking.model.Booking;
import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.model.User;

import java.math.BigDecimal;
import java.sql.Date;

public class BookingTransactionTest {

    public static void main(String[] args) {

        System.out.println(
                "========== BOOKING TRANSACTION TEST =========="
        );

        // -------------------------------------------------
        // Create test User
        // -------------------------------------------------

        User user = new User();

        user.setUserId(1L);


        // -------------------------------------------------
        // Create test Hotel
        // -------------------------------------------------

        Hotel hotel = new Hotel();

        hotel.setHotelId(1L);


        // -------------------------------------------------
        // Create test Room
        // -------------------------------------------------

        Room room = new Room();

        room.setRoomId(1L);
        room.setHotel(hotel);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");


        // -------------------------------------------------
        // Create Booking
        // -------------------------------------------------

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);

        booking.setCheckInDate(
                Date.valueOf("2026-10-10")
        );

        booking.setCheckOutDate(
                Date.valueOf("2026-10-12")
        );

        booking.setGuests(2);

        booking.setTotalAmount(
                new BigDecimal("5000.00")
        );

        booking.setBookingStatus("CONFIRMED");


        // -------------------------------------------------
        // Execute transaction
        // -------------------------------------------------

        BookingService bookingService =
                new BookingServiceImpl();

        boolean result =
                bookingService.createBooking(booking);


        // -------------------------------------------------
        // Result
        // -------------------------------------------------

        if (result) {

            System.out.println(
                    "\n========== BOOKING SUCCESSFUL =========="
            );

            System.out.println(
                    "Booking ID: " +
                            booking.getBookingId()
            );

            System.out.println(
                    "Room Status: " +
                            booking.getRoom().getStatus()
            );

        } else {

            System.out.println(
                    "\n========== BOOKING FAILED =========="
            );
        }

        System.out.println(
                "\n========== TEST COMPLETE =========="
        );
    }
}