package com.booking.service;

import com.booking.model.Review;

import java.util.List;

public interface ReviewService {

    boolean createReview(Review review);

    Review getReviewById(long reviewId);

    List<Review> getAllReviews();

    boolean updateReview(Review review);

    boolean deleteReview(long reviewId);
}