package com.booking.dao;

import com.booking.model.Hotel;

import java.util.List;

public interface HotelDAO {

    boolean create(Hotel hotel);

    Hotel findById(long hotelId);

    List<Hotel> findAll();

    boolean update(Hotel hotel);

    boolean delete(long hotelId);
}