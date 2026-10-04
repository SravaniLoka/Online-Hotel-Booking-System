package com.booking.service;

import com.booking.dao.HotelImageDAO;
import com.booking.dao.HotelImageDAOImpl;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.HotelImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelImageServiceImpl implements HotelImageService {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelImageServiceImpl.class);

    private final HotelImageDAO hotelImageDAO;

    public HotelImageServiceImpl() {
        this.hotelImageDAO = new HotelImageDAOImpl();
    }

    public HotelImageServiceImpl(HotelImageDAO hotelImageDAO) {
        this.hotelImageDAO = hotelImageDAO;
    }

    @Override
    public boolean createHotelImage(HotelImage hotelImage) {

        logger.info("createHotelImage() started");

        // Validation
        if (hotelImage == null) {
            throw new ValidationException("Hotel image cannot be null");
        }

        if (hotelImage.getHotel() == null) {
            throw new ValidationException(
                    "Hotel image hotel cannot be null"
            );
        }

        if (hotelImage.getImageUrl() == null ||
                hotelImage.getImageUrl().trim().isEmpty()) {
            throw new ValidationException(
                    "Image URL cannot be empty"
            );
        }

        boolean created = hotelImageDAO.create(hotelImage);

        if (created) {
            logger.info(
                    "createHotelImage() completed successfully. imageId={}",
                    hotelImage.getImageId()
            );
        } else {
            logger.info("createHotelImage() failed");
        }

        return created;
    }

    @Override
    public HotelImage getHotelImageById(long imageId) {

        logger.info(
                "getHotelImageById() started. imageId={}",
                imageId
        );

        // Validation
        if (imageId <= 0) {
            throw new ValidationException(
                    "Invalid image ID"
            );
        }

        HotelImage hotelImage = hotelImageDAO.findById(imageId);

        if (hotelImage == null) {

            logger.info(
                    "getHotelImageById() completed. Hotel image not found. imageId={}",
                    imageId
            );

            throw new ResourceNotFoundException(
                    "Hotel image not found with ID: " + imageId
            );
        }

        logger.info(
                "getHotelImageById() completed successfully. imageId={}",
                imageId
        );

        return hotelImage;
    }

    @Override
    public List<HotelImage> getAllHotelImages() {

        logger.info("getAllHotelImages() started");

        List<HotelImage> hotelImages =
                hotelImageDAO.findAll();

        logger.info(
                "getAllHotelImages() completed successfully. imagesFound={}",
                hotelImages.size()
        );

        return hotelImages;
    }

    @Override
    public boolean updateHotelImage(HotelImage hotelImage) {

        logger.info("updateHotelImage() started");

        // Validation
        if (hotelImage == null) {
            throw new ValidationException(
                    "Hotel image cannot be null"
            );
        }

        logger.info(
                "updateHotelImage() started. imageId={}",
                hotelImage.getImageId()
        );

        if (hotelImage.getImageId() <= 0) {
            throw new ValidationException(
                    "Invalid image ID"
            );
        }

        if (hotelImage.getHotel() == null) {
            throw new ValidationException(
                    "Hotel image hotel cannot be null"
            );
        }

        if (hotelImage.getImageUrl() == null ||
                hotelImage.getImageUrl().trim().isEmpty()) {
            throw new ValidationException(
                    "Image URL cannot be empty"
            );
        }

        boolean updated = hotelImageDAO.update(hotelImage);

        if (!updated) {

            logger.info(
                    "updateHotelImage() failed. imageId={}",
                    hotelImage.getImageId()
            );

            throw new ResourceNotFoundException(
                    "Hotel image not found with ID: " +
                            hotelImage.getImageId()
            );
        }

        logger.info(
                "updateHotelImage() completed successfully. imageId={}",
                hotelImage.getImageId()
        );

        return true;
    }

    @Override
    public boolean deleteHotelImage(long imageId) {

        logger.info(
                "deleteHotelImage() started. imageId={}",
                imageId
        );

        // Validation
        if (imageId <= 0) {
            throw new ValidationException(
                    "Invalid image ID"
            );
        }

        boolean deleted = hotelImageDAO.delete(imageId);

        if (!deleted) {

            logger.info(
                    "deleteHotelImage() failed. imageId={}",
                    imageId
            );

            throw new ResourceNotFoundException(
                    "Hotel image not found with ID: " + imageId
            );
        }

        logger.info(
                "deleteHotelImage() completed successfully. imageId={}",
                imageId
        );

        return true;
    }
}