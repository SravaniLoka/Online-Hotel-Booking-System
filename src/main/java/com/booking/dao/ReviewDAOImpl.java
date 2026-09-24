package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Review;
import com.booking.model.User;
import com.booking.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public boolean create(Review review) {

        String sql = """
                INSERT INTO review
                (user_id, hotel_id, rating, comment)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, review.getUser().getUserId());
            statement.setLong(2, review.getHotel().getHotelId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        review.setReviewId(keys.getLong(1));
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
    public Review findById(long reviewId) {

        String sql = """
                SELECT review_id, user_id, hotel_id, rating, comment
                FROM review
                WHERE review_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, reviewId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToReview(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Review> findAll() {

        List<Review> reviews = new ArrayList<>();

        String sql = """
                SELECT review_id, user_id, hotel_id, rating, comment
                FROM review
                ORDER BY review_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                reviews.add(
                        mapResultSetToReview(resultSet)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public boolean update(Review review) {

        String sql = """
                UPDATE review
                SET user_id = ?,
                    hotel_id = ?,
                    rating = ?,
                    comment = ?
                WHERE review_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, review.getUser().getUserId());
            statement.setLong(2, review.getHotel().getHotelId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setLong(5, review.getReviewId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long reviewId) {

        String sql = """
                DELETE FROM review
                WHERE review_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, reviewId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Review mapResultSetToReview(ResultSet resultSet)
            throws SQLException {

        User user = new User();
        user.setUserId(
                resultSet.getLong("user_id")
        );

        Hotel hotel = new Hotel();
        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        return new Review(
                resultSet.getLong("review_id"),
                user,
                hotel,
                resultSet.getInt("rating"),
                resultSet.getString("comment")
        );
    }
}