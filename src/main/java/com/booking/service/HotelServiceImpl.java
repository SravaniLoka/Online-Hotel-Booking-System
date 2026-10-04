package com.booking.service;

import com.booking.dao.HotelDAO;
import com.booking.dao.HotelDAOImpl;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
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

    public HotelServiceImpl(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }

    @Override
    public boolean createHotel(Hotel hotel) {

        logger.info("createHotel() started");

        // Validation
        if (hotel == null) {
            throw new ValidationException("Hotel cannot be null");
        }

        if (hotel.getName() == null ||
                hotel.getName().trim().isEmpty()) {
            throw new ValidationException("Hotel name cannot be empty");
        }

        if (hotel.getAddress() == null ||
                hotel.getAddress().trim().isEmpty()) {
            throw new ValidationException("Hotel address cannot be empty");
        }

        if (hotel.getStatus() == null ||
                hotel.getStatus().trim().isEmpty()) {
            throw new ValidationException("Hotel status cannot be empty");
        }

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

        // Validation
        if (hotelId <= 0) {
            throw new ValidationException("Invalid hotel ID");
        }

        Hotel hotel = hotelDAO.findById(hotelId);

        if (hotel == null) {
            logger.info(
                    "getHotelById() completed. Hotel not found. hotelId={}",
                    hotelId
            );

            throw new ResourceNotFoundException(
                    "Hotel not found with ID: " + hotelId
            );
        }

        logger.info(
                "getHotelById() completed successfully. hotelId={}",
                hotelId
        );

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

        logger.info("updateHotel() started");

        // Validation
        if (hotel == null) {
            throw new ValidationException("Hotel cannot be null");
        }

        logger.info(
                "updateHotel() started. hotelId={}",
                hotel.getHotelId()
        );

        if (hotel.getHotelId() <= 0) {
            throw new ValidationException("Invalid hotel ID");
        }

        if (hotel.getName() == null ||
                hotel.getName().trim().isEmpty()) {
            throw new ValidationException("Hotel name cannot be empty");
        }

        if (hotel.getAddress() == null ||
                hotel.getAddress().trim().isEmpty()) {
            throw new ValidationException("Hotel address cannot be empty");
        }

        if (hotel.getStatus() == null ||
                hotel.getStatus().trim().isEmpty()) {
            throw new ValidationException("Hotel status cannot be empty");
        }

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

            throw new ResourceNotFoundException(
                    "Hotel not found with ID: " + hotel.getHotelId()
            );
        }

        return true;
    }

    @Override
    public boolean deleteHotel(long hotelId) {

        logger.info(
                "deleteHotel() started. hotelId={}",
                hotelId
        );

        // Validation
        if (hotelId <= 0) {
            throw new ValidationException("Invalid hotel ID");
        }

        boolean deleted = hotelDAO.delete(hotelId);

        if (!deleted) {
            logger.info(
                    "deleteHotel() failed. hotelId={}",
                    hotelId
            );

            throw new ResourceNotFoundException(
                    "Hotel not found with ID: " + hotelId
            );
        }

        logger.info(
                "deleteHotel() completed successfully. hotelId={}",
                hotelId
        );

        return true;
    }
}