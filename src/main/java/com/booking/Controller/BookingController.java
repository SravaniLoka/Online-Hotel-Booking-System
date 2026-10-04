package com.booking.Controller;

import com.booking.model.Booking;
import com.booking.service.BookingService;
import com.booking.service.BookingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookingController {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController() {
        this.bookingService = new BookingServiceImpl();
    }

    public boolean createBooking(Booking booking) {

        logger.info("createBooking() requested");

        boolean created = bookingService.createBooking(booking);

        if (created) {
            logger.info(
                    "createBooking() completed successfully. bookingId={}",
                    booking.getBookingId()
            );
        } else {
            logger.info("createBooking() failed");
        }

        return created;
    }

    public Booking getBookingById(long bookingId) {

        logger.info(
                "getBookingById() requested. bookingId={}",
                bookingId
        );

        Booking booking = bookingService.getBookingById(bookingId);

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

    public List<Booking> getAllBookings() {

        logger.info("getAllBookings() requested");

        List<Booking> bookings = bookingService.getAllBookings();

        logger.info(
                "getAllBookings() completed successfully. bookingsFound={}",
                bookings.size()
        );

        return bookings;
    }

    public boolean updateBooking(Booking booking) {

        logger.info(
                "updateBooking() requested. bookingId={}",
                booking.getBookingId()
        );

        boolean updated = bookingService.updateBooking(booking);

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

    public boolean deleteBooking(long bookingId) {

        logger.info(
                "deleteBooking() requested. bookingId={}",
                bookingId
        );

        boolean deleted = bookingService.deleteBooking(bookingId);

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