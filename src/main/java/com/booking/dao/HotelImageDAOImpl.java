package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.HotelImage;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelImageDAOImpl implements HotelImageDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelImageDAOImpl.class);

    // SQL queries
    private static final String HOTEL_IMAGE_CREATE_SQL = """
            INSERT INTO hotel_image
            (hotel_id, image_url, caption)
            VALUES (?, ?, ?)
            """;

    private static final String HOTEL_IMAGE_FIND_BY_ID_SQL = """
            SELECT image_id, hotel_id, image_url, caption
            FROM hotel_image
            WHERE image_id = ?
            """;

    private static final String HOTEL_IMAGE_FIND_ALL_SQL = """
            SELECT image_id, hotel_id, image_url, caption
            FROM hotel_image
            ORDER BY image_id
            """;

    private static final String HOTEL_IMAGE_UPDATE_SQL = """
            UPDATE hotel_image
            SET hotel_id = ?,
                image_url = ?,
                caption = ?
            WHERE image_id = ?
            """;

    private static final String HOTEL_IMAGE_DELETE_SQL = """
            DELETE FROM hotel_image
            WHERE image_id = ?
            """;

    @Override
    public boolean create(HotelImage hotelImage) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     HOTEL_IMAGE_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    hotelImage.getHotel().getHotelId()
            );
            statement.setString(
                    2,
                    hotelImage.getImageUrl()
            );
            statement.setString(
                    3,
                    hotelImage.getCaption()
            );

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        hotelImage.setImageId(keys.getLong(1));
                    }
                }

                logger.info(
                        "create() completed successfully. imageId={}",
                        hotelImage.getImageId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating hotel image",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public HotelImage findById(long imageId) {

        logger.info(
                "findById() started. imageId={}",
                imageId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             HOTEL_IMAGE_FIND_BY_ID_SQL)) {

            statement.setLong(1, imageId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    HotelImage hotelImage =
                            mapResultSetToHotelImage(resultSet);

                    logger.info(
                            "findById() completed successfully. imageId={}",
                            imageId
                    );

                    return hotelImage;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding hotel image. imageId={}",
                    imageId,
                    e
            );
        }

        logger.info(
                "findById() completed. Hotel image not found. imageId={}",
                imageId
        );

        return null;
    }

    @Override
    public List<HotelImage> findAll() {

        logger.info("findAll() started");

        List<HotelImage> hotelImages = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             HOTEL_IMAGE_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                hotelImages.add(
                        mapResultSetToHotelImage(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. hotelImagesFound={}",
                    hotelImages.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all hotel images",
                    e
            );
        }

        return hotelImages;
    }

    @Override
    public boolean update(HotelImage hotelImage) {

        logger.info(
                "update() started. imageId={}",
                hotelImage.getImageId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             HOTEL_IMAGE_UPDATE_SQL)) {

            statement.setLong(
                    1,
                    hotelImage.getHotel().getHotelId()
            );
            statement.setString(
                    2,
                    hotelImage.getImageUrl()
            );
            statement.setString(
                    3,
                    hotelImage.getCaption()
            );
            statement.setLong(
                    4,
                    hotelImage.getImageId()
            );

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. imageId={}",
                        hotelImage.getImageId()
                );

            } else {

                logger.info(
                        "update() completed. No hotel image updated. imageId={}",
                        hotelImage.getImageId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating hotel image. imageId={}",
                    hotelImage.getImageId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long imageId) {

        logger.info(
                "delete() started. imageId={}",
                imageId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             HOTEL_IMAGE_DELETE_SQL)) {

            statement.setLong(1, imageId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. imageId={}",
                        imageId
                );

            } else {

                logger.info(
                        "delete() completed. No hotel image deleted. imageId={}",
                        imageId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting hotel image. imageId={}",
                    imageId,
                    e
            );
        }

        return false;
    }

    private HotelImage mapResultSetToHotelImage(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToHotelImage() started");

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        HotelImage hotelImage = new HotelImage(
                resultSet.getLong("image_id"),
                hotel,
                resultSet.getString("image_url"),
                resultSet.getString("caption")
        );

        logger.info(
                "mapResultSetToHotelImage() completed successfully. imageId={}",
                hotelImage.getImageId()
        );

        return hotelImage;
    }
}