package com.booking.dao;

import com.booking.model.HotelImage;

import java.util.List;

public interface HotelImageDAO {

    boolean create(HotelImage hotelImage);

    HotelImage findById(long imageId);

    List<HotelImage> findAll();

    boolean update(HotelImage hotelImage);

    boolean delete(long imageId);
}