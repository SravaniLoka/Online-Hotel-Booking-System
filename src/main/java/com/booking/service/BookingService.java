package com.booking.service;

import com.booking.model.Booking;

import java.util.List;

public interface BookingService {

    boolean createBooking(Booking booking);

    Booking getBookingById(long bookingId);

    List<Booking> getAllBookings();

    boolean updateBooking(Booking booking);

    boolean deleteBooking(long bookingId);
}