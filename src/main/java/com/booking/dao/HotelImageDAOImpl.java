package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.HotelImage;
import com.booking.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelImageDAOImpl implements HotelImageDAO {

    @Override
    public boolean create(HotelImage hotelImage) {

        String sql = """
                INSERT INTO hotel_image
                (hotel_id, image_url, caption)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, hotelImage.getHotel().getHotelId());
            statement.setString(2, hotelImage.getImageUrl());
            statement.setString(3, hotelImage.getCaption());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        hotelImage.setImageId(keys.getLong(1));
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
    public HotelImage findById(long imageId) {

        String sql = """
                SELECT image_id, hotel_id, image_url, caption
                FROM hotel_image
                WHERE image_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, imageId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToHotelImage(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<HotelImage> findAll() {

        List<HotelImage> hotelImages = new ArrayList<>();

        String sql = """
                SELECT image_id, hotel_id, image_url, caption
                FROM hotel_image
                ORDER BY image_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                hotelImages.add(
                        mapResultSetToHotelImage(resultSet)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelImages;
    }

    @Override
    public boolean update(HotelImage hotelImage) {

        String sql = """
                UPDATE hotel_image
                SET hotel_id = ?,
                    image_url = ?,
                    caption = ?
                WHERE image_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, hotelImage.getHotel().getHotelId());
            statement.setString(2, hotelImage.getImageUrl());
            statement.setString(3, hotelImage.getCaption());
            statement.setLong(4, hotelImage.getImageId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long imageId) {

        String sql = """
                DELETE FROM hotel_image
                WHERE image_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, imageId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private HotelImage mapResultSetToHotelImage(ResultSet resultSet)
            throws SQLException {

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        return new HotelImage(
                resultSet.getLong("image_id"),
                hotel,
                resultSet.getString("image_url"),
                resultSet.getString("caption")
        );
    }
}