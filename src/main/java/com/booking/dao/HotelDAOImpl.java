package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Location;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(HotelDAOImpl.class);

    // SQL queries
    private static final String HOTEL_CREATE_SQL = """
            INSERT INTO hotel
            (location_id, name, description, address,
             star_rating, amenities, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String HOTEL_FIND_BY_ID_SQL = """
            SELECT hotel_id, location_id, name, description,
                   address, star_rating, amenities, status
            FROM hotel
            WHERE hotel_id = ?
            """;

    private static final String HOTEL_FIND_ALL_SQL = """
            SELECT hotel_id, location_id, name, description,
                   address, star_rating, amenities, status
            FROM hotel
            ORDER BY hotel_id
            """;

    private static final String HOTEL_UPDATE_SQL = """
            UPDATE hotel
            SET location_id = ?,
                name = ?,
                description = ?,
                address = ?,
                star_rating = ?,
                amenities = ?,
                status = ?
            WHERE hotel_id = ?
            """;

    private static final String HOTEL_DELETE_SQL =
            "DELETE FROM hotel WHERE hotel_id = ?";

    @Override
    public boolean create(Hotel hotel) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     HOTEL_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    hotel.getLocation().getLocationId()
            );
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getDescription());
            statement.setString(4, hotel.getAddress());
            statement.setBigDecimal(5, hotel.getStarRating());
            statement.setString(6, hotel.getAmenities());
            statement.setString(7, hotel.getStatus());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        hotel.setHotelId(keys.getLong(1));
                    }
                }

                logger.info(
                        "create() completed successfully. hotelId={}",
                        hotel.getHotelId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating hotel",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public Hotel findById(long hotelId) {

        logger.info(
                "findById() started. hotelId={}",
                hotelId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(HOTEL_FIND_BY_ID_SQL)) {

            statement.setLong(1, hotelId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Hotel hotel =
                            mapResultSetToHotel(resultSet);

                    logger.info(
                            "findById() completed successfully. hotelId={}",
                            hotelId
                    );

                    return hotel;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding hotel. hotelId={}",
                    hotelId,
                    e
            );
        }

        logger.info(
                "findById() completed. Hotel not found. hotelId={}",
                hotelId
        );

        return null;
    }

    @Override
    public List<Hotel> findAll() {

        logger.info("findAll() started");

        List<Hotel> hotels = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(HOTEL_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                hotels.add(
                        mapResultSetToHotel(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. hotelsFound={}",
                    hotels.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all hotels",
                    e
            );
        }

        return hotels;
    }

    @Override
    public boolean update(Hotel hotel) {

        logger.info(
                "update() started. hotelId={}",
                hotel.getHotelId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(HOTEL_UPDATE_SQL)) {

            statement.setLong(
                    1,
                    hotel.getLocation().getLocationId()
            );
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getDescription());
            statement.setString(4, hotel.getAddress());
            statement.setBigDecimal(5, hotel.getStarRating());
            statement.setString(6, hotel.getAmenities());
            statement.setString(7, hotel.getStatus());
            statement.setLong(8, hotel.getHotelId());

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. hotelId={}",
                        hotel.getHotelId()
                );

            } else {

                logger.info(
                        "update() completed. No hotel updated. hotelId={}",
                        hotel.getHotelId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating hotel. hotelId={}",
                    hotel.getHotelId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long hotelId) {

        logger.info(
                "delete() started. hotelId={}",
                hotelId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(HOTEL_DELETE_SQL)) {

            statement.setLong(1, hotelId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. hotelId={}",
                        hotelId
                );

            } else {

                logger.info(
                        "delete() completed. No hotel deleted. hotelId={}",
                        hotelId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting hotel. hotelId={}",
                    hotelId,
                    e
            );
        }

        return false;
    }

    private Hotel mapResultSetToHotel(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToHotel() started");

        Location location = new Location();

        location.setLocationId(
                resultSet.getLong("location_id")
        );

        Hotel hotel = new Hotel(
                resultSet.getLong("hotel_id"),
                location,
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("address"),
                resultSet.getBigDecimal("star_rating"),
                resultSet.getString("amenities"),
                resultSet.getString("status")
        );

        logger.info(
                "mapResultSetToHotel() completed successfully. hotelId={}",
                hotel.getHotelId()
        );

        return hotel;
    }
}