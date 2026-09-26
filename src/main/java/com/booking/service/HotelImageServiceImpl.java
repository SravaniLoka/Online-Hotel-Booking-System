package com.booking.service;

import com.booking.dao.HotelImageDAO;
import com.booking.dao.HotelImageDAOImpl;
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

    @Override
    public boolean createHotelImage(HotelImage hotelImage) {

        logger.info("createHotelImage() started");

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

        HotelImage hotelImage = hotelImageDAO.findById(imageId);

        if (hotelImage != null) {
            logger.info(
                    "getHotelImageById() completed successfully. imageId={}",
                    imageId
            );
        } else {
            logger.info(
                    "getHotelImageById() completed. Hotel image not found. imageId={}",
                    imageId
            );
        }

        return hotelImage;
    }

    @Override
    public List<HotelImage> getAllHotelImages() {

        logger.info("getAllHotelImages() started");

        List<HotelImage> hotelImages = hotelImageDAO.findAll();

        logger.info(
                "getAllHotelImages() completed successfully. imagesFound={}",
                hotelImages.size()
        );

        return hotelImages;
    }

    @Override
    public boolean updateHotelImage(HotelImage hotelImage) {

        logger.info(
                "updateHotelImage() started. imageId={}",
                hotelImage.getImageId()
        );

        boolean updated = hotelImageDAO.update(hotelImage);

        if (updated) {
            logger.info(
                    "updateHotelImage() completed successfully. imageId={}",
                    hotelImage.getImageId()
            );
        } else {
            logger.info(
                    "updateHotelImage() failed. imageId={}",
                    hotelImage.getImageId()
            );
        }

        return updated;
    }

    @Override
    public boolean deleteHotelImage(long imageId) {

        logger.info(
                "deleteHotelImage() started. imageId={}",
                imageId
        );

        boolean deleted = hotelImageDAO.delete(imageId);

        if (deleted) {
            logger.info(
                    "deleteHotelImage() completed successfully. imageId={}",
                    imageId
            );
        } else {
            logger.info(
                    "deleteHotelImage() failed. imageId={}",
                    imageId
            );
        }

        return deleted;
    }
}