package com.booking.service;

import com.booking.dao.HotelDAO;
import com.booking.dao.HotelDAOImpl;
import com.booking.model.Hotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelServiceImpl implements HotelService {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelServiceImpl.class);

    private final HotelDAO hotelDAO;

    public HotelServiceImpl() {
        this.hotelDAO = new HotelDAOImpl();
    }

    @Override
    public boolean createHotel(Hotel hotel) {

        logger.info("createHotel() started");

        boolean created = hotelDAO.create(hotel);

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

    @Override
    public Hotel getHotelById(long hotelId) {

        logger.info(
                "getHotelById() started. hotelId={}",
                hotelId
        );

        Hotel hotel = hotelDAO.findById(hotelId);

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

    @Override
    public List<Hotel> getAllHotels() {

        logger.info("getAllHotels() started");

        List<Hotel> hotels = hotelDAO.findAll();

        logger.info(
                "getAllHotels() completed successfully. hotelsFound={}",
                hotels.size()
        );

        return hotels;
    }

    @Override
    public boolean updateHotel(Hotel hotel) {

        logger.info(
                "updateHotel() started. hotelId={}",
                hotel.getHotelId()
        );

        boolean updated = hotelDAO.update(hotel);

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

    @Override
    public boolean deleteHotel(long hotelId) {

        logger.info(
                "deleteHotel() started. hotelId={}",
                hotelId
        );

        boolean deleted = hotelDAO.delete(hotelId);

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