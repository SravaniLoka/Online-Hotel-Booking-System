package com.booking.dao;

import com.booking.model.Booking;

import java.sql.Connection;
import java.util.List;

public interface BookingDAO {

    boolean create(Booking booking);

    boolean create(Booking booking, Connection connection);

    Booking findById(long bookingId);

    List<Booking> findAll();

    boolean update(Booking booking);

    boolean update(Booking booking, Connection connection);

    boolean delete(long bookingId);
}