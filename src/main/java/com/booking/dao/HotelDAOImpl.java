package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Location;
import com.booking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {

    @Override
    public boolean create(Hotel hotel) {

        String sql = """
                INSERT INTO hotel
                (location_id, name, description, address,
                 star_rating, amenities, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, hotel.getLocation().getLocationId());
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

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Hotel findById(long hotelId) {

        String sql = """
                SELECT hotel_id, location_id, name, description,
                       address, star_rating, amenities, status
                FROM hotel
                WHERE hotel_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, hotelId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToHotel(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Hotel> findAll() {

        List<Hotel> hotels = new ArrayList<>();

        String sql = """
                SELECT hotel_id, location_id, name, description,
                       address, star_rating, amenities, status
                FROM hotel
                ORDER BY hotel_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                hotels.add(mapResultSetToHotel(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    @Override
    public boolean update(Hotel hotel) {

        String sql = """
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

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, hotel.getLocation().getLocationId());
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getDescription());
            statement.setString(4, hotel.getAddress());
            statement.setBigDecimal(5, hotel.getStarRating());
            statement.setString(6, hotel.getAmenities());
            statement.setString(7, hotel.getStatus());
            statement.setLong(8, hotel.getHotelId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long hotelId) {

        String sql = "DELETE FROM hotel WHERE hotel_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, hotelId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Hotel mapResultSetToHotel(ResultSet resultSet) throws SQLException {

        Location location = new Location();
        location.setLocationId(resultSet.getLong("location_id"));

        return new Hotel(
                resultSet.getLong("hotel_id"),
                location,
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("address"),
                resultSet.getBigDecimal("star_rating"),
                resultSet.getString("amenities"),
                resultSet.getString("status")
        );
    }
}