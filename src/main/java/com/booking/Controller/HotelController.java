package com.booking.Controller;

import com.booking.model.Hotel;
import com.booking.service.HotelService;
import com.booking.service.HotelServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelController {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    public HotelController() {
        this.hotelService = new HotelServiceImpl();
    }

    public boolean createHotel(Hotel hotel) {

        logger.info("createHotel() requested");

        boolean created = hotelService.createHotel(hotel);

        if (created) {
            logger.info(
                    "createHotel() completed successfully. hotelId={}",
                    hotel.getHotelId()
            );
        } else {
            logger.info("createHotel() failed");
        }

        return created;
    }

    public Hotel getHotelById(long hotelId) {

        logger.info(
                "getHotelById() requested. hotelId={}",
                hotelId
        );

        Hotel hotel = hotelService.getHotelById(hotelId);

        if (hotel != null) {
            logger.info(
                    "getHotelById() completed successfully. hotelId={}",
                    hotelId
            );
        } else {
            logger.info(
                    "getHotelById() completed. Hotel not found. hotelId={}",
                    hotelId
            );
        }

        return hotel;
    }

    public List<Hotel> getAllHotels() {

        logger.info("getAllHotels() requested");

        List<Hotel> hotels = hotelService.getAllHotels();

        logger.info(
                "getAllHotels() completed successfully. hotelsFound={}",
                hotels.size()
        );

        return hotels;
    }

    public boolean updateHotel(Hotel hotel) {

        logger.info(
                "updateHotel() requested. hotelId={}",
                hotel.getHotelId()
        );

        boolean updated = hotelService.updateHotel(hotel);

        if (updated) {
            logger.info(
                    "updateHotel() completed successfully. hotelId={}",
                    hotel.getHotelId()
            );
        } else {
            logger.info(
                    "updateHotel() failed. hotelId={}",
                    hotel.getHotelId()
            );
        }

        return updated;
    }

    public boolean deleteHotel(long hotelId) {

        logger.info(
                "deleteHotel() requested. hotelId={}",
                hotelId
        );

        boolean deleted = hotelService.deleteHotel(hotelId);

        if (deleted) {
            logger.info(
                    "deleteHotel() completed successfully. hotelId={}",
                    hotelId
            );
        } else {
            logger.info(
                    "deleteHotel() failed. hotelId={}",
                    hotelId
            );
        }

        return deleted;
    }
}