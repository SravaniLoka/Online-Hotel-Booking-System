package com.booking.service;

import com.booking.dao.ReviewDAO;
import com.booking.dao.ReviewDAOImpl;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
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

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public boolean createReview(Review review) {

        logger.info("createReview() started");

        // Validation
        if (review == null) {
            throw new ValidationException("Review cannot be null");
        }

        if (review.getUser() == null) {
            throw new ValidationException("Review user cannot be null");
        }

        if (review.getHotel() == null) {
            throw new ValidationException("Review hotel cannot be null");
        }

        if (review.getRating() < 1 ||
                review.getRating() > 5) {
            throw new ValidationException(
                    "Review rating must be between 1 and 5"
            );
        }

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

        // Validation
        if (reviewId <= 0) {
            throw new ValidationException(
                    "Invalid review ID"
            );
        }

        Review review = reviewDAO.findById(reviewId);

        if (review == null) {
            logger.info(
                    "getReviewById() completed. Review not found. reviewId={}",
                    reviewId
            );

            throw new ResourceNotFoundException(
                    "Review not found with ID: " + reviewId
            );
        }

        logger.info(
                "getReviewById() completed successfully. reviewId={}",
                reviewId
        );

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

        logger.info("updateReview() started");

        // Validation
        if (review == null) {
            throw new ValidationException(
                    "Review cannot be null"
            );
        }

        logger.info(
                "updateReview() started. reviewId={}",
                review.getReviewId()
        );

        if (review.getReviewId() <= 0) {
            throw new ValidationException(
                    "Invalid review ID"
            );
        }

        if (review.getUser() == null) {
            throw new ValidationException(
                    "Review user cannot be null"
            );
        }

        if (review.getHotel() == null) {
            throw new ValidationException(
                    "Review hotel cannot be null"
            );
        }

        if (review.getRating() < 1 ||
                review.getRating() > 5) {
            throw new ValidationException(
                    "Review rating must be between 1 and 5"
            );
        }

        boolean updated = reviewDAO.update(review);

        if (!updated) {
            logger.info(
                    "updateReview() failed. reviewId={}",
                    review.getReviewId()
            );

            throw new ResourceNotFoundException(
                    "Review not found with ID: " +
                            review.getReviewId()
            );
        }

        logger.info(
                "updateReview() completed successfully. reviewId={}",
                review.getReviewId()
        );

        return true;
    }

    @Override
    public boolean deleteReview(long reviewId) {

        logger.info(
                "deleteReview() started. reviewId={}",
                reviewId
        );

        // Validation
        if (reviewId <= 0) {
            throw new ValidationException(
                    "Invalid review ID"
            );
        }

        boolean deleted = reviewDAO.delete(reviewId);

        if (!deleted) {
            logger.info(
                    "deleteReview() failed. reviewId={}",
                    reviewId
            );

            throw new ResourceNotFoundException(
                    "Review not found with ID: " + reviewId
            );
        }

        logger.info(
                "deleteReview() completed successfully. reviewId={}",
                reviewId
        );

        return true;
    }
}