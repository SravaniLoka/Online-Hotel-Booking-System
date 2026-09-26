package com.booking.service;

import com.booking.model.Hotel;

import java.util.List;

public interface HotelService {

    boolean createHotel(Hotel hotel);

    Hotel getHotelById(long hotelId);

    List<Hotel> getAllHotels();

    boolean updateHotel(Hotel hotel);

    boolean deleteHotel(long hotelId);
}