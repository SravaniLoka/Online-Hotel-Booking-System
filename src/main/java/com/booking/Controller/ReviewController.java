package com.booking.Controller;

import com.booking.model.Review;
import com.booking.service.ReviewService;
import com.booking.service.ReviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReviewController {

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController() {
        this.reviewService = new ReviewServiceImpl();
    }

    public boolean createReview(Review review) {

        logger.info("createReview() requested");

        boolean created = reviewService.createReview(review);

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

    public Review getReviewById(long reviewId) {

        logger.info(
                "getReviewById() requested. reviewId={}",
                reviewId
        );

        Review review = reviewService.getReviewById(reviewId);

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

    public List<Review> getAllReviews() {

        logger.info("getAllReviews() requested");

        List<Review> reviews = reviewService.getAllReviews();

        logger.info(
                "getAllReviews() completed successfully. reviewsFound={}",
                reviews.size()
        );

        return reviews;
    }

    public boolean updateReview(Review review) {

        logger.info(
                "updateReview() requested. reviewId={}",
                review.getReviewId()
        );

        boolean updated = reviewService.updateReview(review);

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

    public boolean deleteReview(long reviewId) {

        logger.info(
                "deleteReview() requested. reviewId={}",
                reviewId
        );

        boolean deleted = reviewService.deleteReview(reviewId);

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