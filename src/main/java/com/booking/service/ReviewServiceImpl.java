package com.booking.service;

import com.booking.dao.ReviewDAO;
import com.booking.dao.ReviewDAOImpl;
import com.booking.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    @Override
    public boolean createReview(Review review) {

        logger.info("createReview() started");

        boolean created = reviewDAO.create(review);

        if (created) {
            logger.info(
                    "createReview() completed successfully. reviewId={}",
                    review.getReviewId()
            );
        } else {
            logger.info("createReview() failed");
        }

        return created;
    }

    @Override
    public Review getReviewById(long reviewId) {

        logger.info(
                "getReviewById() started. reviewId={}",
                reviewId
        );

        Review review = reviewDAO.findById(reviewId);

        if (review != null) {
            logger.info(
                    "getReviewById() completed successfully. reviewId={}",
                    reviewId
            );
        } else {
            logger.info(
                    "getReviewById() completed. Review not found. reviewId={}",
                    reviewId
            );
        }

        return review;
    }

    @Override
    public List<Review> getAllReviews() {

        logger.info("getAllReviews() started");

        List<Review> reviews = reviewDAO.findAll();

        logger.info(
                "getAllReviews() completed successfully. reviewsFound={}",
                reviews.size()
        );

        return reviews;
    }

    @Override
    public boolean updateReview(Review review) {

        logger.info(
                "updateReview() started. reviewId={}",
                review.getReviewId()
        );

        boolean updated = reviewDAO.update(review);

        if (updated) {
            logger.info(
                    "updateReview() completed successfully. reviewId={}",
                    review.getReviewId()
            );
        } else {
            logger.info(
                    "updateReview() failed. reviewId={}",
                    review.getReviewId()
            );
        }

        return updated;
    }

    @Override
    public boolean deleteReview(long reviewId) {

        logger.info(
                "deleteReview() started. reviewId={}",
                reviewId
        );

        boolean deleted = reviewDAO.delete(reviewId);

        if (deleted) {
            logger.info(
                    "deleteReview() completed successfully. reviewId={}",
                    reviewId
            );
        } else {
            logger.info(
                    "deleteReview() failed. reviewId={}",
                    reviewId
            );
        }

        return deleted;
    }
}