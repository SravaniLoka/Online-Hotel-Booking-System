package com.booking.service;

import com.booking.model.HotelImage;

import java.util.List;

public interface HotelImageService {

    boolean createHotelImage(HotelImage hotelImage);

    HotelImage getHotelImageById(long imageId);

    List<HotelImage> getAllHotelImages();

    boolean updateHotelImage(HotelImage hotelImage);

    boolean deleteHotelImage(long imageId);
}