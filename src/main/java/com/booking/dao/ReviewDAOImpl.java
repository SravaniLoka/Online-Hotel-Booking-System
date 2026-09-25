package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Review;
import com.booking.model.User;
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

public class ReviewDAOImpl implements ReviewDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewDAOImpl.class);

    // SQL queries
    private static final String REVIEW_CREATE_SQL = """
            INSERT INTO review
            (user_id, hotel_id, rating, comment)
            VALUES (?, ?, ?, ?)
            """;

    private static final String REVIEW_FIND_BY_ID_SQL = """
            SELECT review_id, user_id, hotel_id, rating, comment
            FROM review
            WHERE review_id = ?
            """;

    private static final String REVIEW_FIND_ALL_SQL = """
            SELECT review_id, user_id, hotel_id, rating, comment
            FROM review
            ORDER BY review_id
            """;

    private static final String REVIEW_UPDATE_SQL = """
            UPDATE review
            SET user_id = ?,
                hotel_id = ?,
                rating = ?,
                comment = ?
            WHERE review_id = ?
            """;

    private static final String REVIEW_DELETE_SQL = """
            DELETE FROM review
            WHERE review_id = ?
            """;

    @Override
    public boolean create(Review review) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     REVIEW_CREATE_SQL,
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

                logger.info(
                        "create() completed successfully. reviewId={}",
                        review.getReviewId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating review",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public Review findById(long reviewId) {

        logger.info(
                "findById() started. reviewId={}",
                reviewId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(REVIEW_FIND_BY_ID_SQL)) {

            statement.setLong(1, reviewId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Review review =
                            mapResultSetToReview(resultSet);

                    logger.info(
                            "findById() completed successfully. reviewId={}",
                            reviewId
                    );

                    return review;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding review. reviewId={}",
                    reviewId,
                    e
            );
        }

        logger.info(
                "findById() completed. Review not found. reviewId={}",
                reviewId
        );

        return null;
    }

    @Override
    public List<Review> findAll() {

        logger.info("findAll() started");

        List<Review> reviews = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(REVIEW_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                reviews.add(
                        mapResultSetToReview(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. reviewsFound={}",
                    reviews.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all reviews",
                    e
            );
        }

        return reviews;
    }

    @Override
    public boolean update(Review review) {

        logger.info(
                "update() started. reviewId={}",
                review.getReviewId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(REVIEW_UPDATE_SQL)) {

            statement.setLong(1, review.getUser().getUserId());
            statement.setLong(2, review.getHotel().getHotelId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setLong(5, review.getReviewId());

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. reviewId={}",
                        review.getReviewId()
                );

            } else {

                logger.info(
                        "update() completed. No review updated. reviewId={}",
                        review.getReviewId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating review. reviewId={}",
                    review.getReviewId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long reviewId) {

        logger.info(
                "delete() started. reviewId={}",
                reviewId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(REVIEW_DELETE_SQL)) {

            statement.setLong(1, reviewId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. reviewId={}",
                        reviewId
                );

            } else {

                logger.info(
                        "delete() completed. No review deleted. reviewId={}",
                        reviewId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting review. reviewId={}",
                    reviewId,
                    e
            );
        }

        return false;
    }

    private Review mapResultSetToReview(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToReview() started");

        User user = new User();

        user.setUserId(
                resultSet.getLong("user_id")
        );

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        Review review = new Review(
                resultSet.getLong("review_id"),
                user,
                hotel,
                resultSet.getInt("rating"),
                resultSet.getString("comment")
        );

        logger.info(
                "mapResultSetToReview() completed successfully. reviewId={}",
                review.getReviewId()
        );

        return review;
    }
}