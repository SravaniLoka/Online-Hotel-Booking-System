package com.booking.Controller;

import com.booking.model.HotelImage;
import com.booking.service.HotelImageService;
import com.booking.service.HotelImageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelImageController {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelImageController.class);

    private final HotelImageService hotelImageService;

    public HotelImageController() {
        this.hotelImageService = new HotelImageServiceImpl();
    }

    public boolean createHotelImage(HotelImage hotelImage) {

        logger.info("createHotelImage() requested");

        boolean created = hotelImageService.createHotelImage(hotelImage);

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

    public HotelImage getHotelImageById(long imageId) {

        logger.info(
                "getHotelImageById() requested. imageId={}",
                imageId
        );

        HotelImage hotelImage =
                hotelImageService.getHotelImageById(imageId);

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

    public List<HotelImage> getAllHotelImages() {

        logger.info("getAllHotelImages() requested");

        List<HotelImage> hotelImages =
                hotelImageService.getAllHotelImages();

        logger.info(
                "getAllHotelImages() completed successfully. imagesFound={}",
                hotelImages.size()
        );

        return hotelImages;
    }

    public boolean updateHotelImage(HotelImage hotelImage) {

        logger.info(
                "updateHotelImage() requested. imageId={}",
                hotelImage.getImageId()
        );

        boolean updated =
                hotelImageService.updateHotelImage(hotelImage);

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

    public boolean deleteHotelImage(long imageId) {

        logger.info(
                "deleteHotelImage() requested. imageId={}",
                imageId
        );

        boolean deleted =
                hotelImageService.deleteHotelImage(imageId);

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