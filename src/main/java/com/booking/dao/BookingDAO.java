package com.booking.dao;

import com.booking.model.Booking;

import java.util.List;

public interface BookingDAO {

    boolean create(Booking booking);

    Booking findById(long bookingId);

    List<Booking> findAll();

    boolean update(Booking booking);

    boolean delete(long bookingId);
}