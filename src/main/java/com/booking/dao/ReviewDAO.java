package com.booking.dao;

import com.booking.model.Review;

import java.util.List;

public interface ReviewDAO {

    boolean create(Review review);

    Review findById(long reviewId);

    List<Review> findAll();

    boolean update(Review review);

    boolean delete(long reviewId);
}