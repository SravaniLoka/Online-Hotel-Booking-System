package com.booking.test;

import com.booking.dao.ReviewDAO;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Hotel;
import com.booking.model.Review;
import com.booking.model.User;
import com.booking.service.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewDAO reviewDAO;

    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewServiceImpl(reviewDAO);
    }

    // =========================================================
    // Helper method
    // =========================================================

    private Review createValidReview() {

        Review review = new Review();

        User user = new User();
        Hotel hotel = new Hotel();

        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(5);
        review.setComment("Excellent hotel");

        return review;
    }

    // =========================================================
    // CREATE REVIEW
    // =========================================================

    @Test
    void createReview_success() {

        Review review = createValidReview();

        when(reviewDAO.create(review)).thenReturn(true);

        boolean result = reviewService.createReview(review);

        assertTrue(result);

        verify(reviewDAO, times(1)).create(review);
    }

    @Test
    void createReview_failure() {

        Review review = createValidReview();

        when(reviewDAO.create(review)).thenReturn(false);

        boolean result = reviewService.createReview(review);

        assertFalse(result);

        verify(reviewDAO, times(1)).create(review);
    }

    // =========================================================
    // GET REVIEW BY ID
    // =========================================================

    @Test
    void getReviewById_reviewFound() {

        long reviewId = 1L;

        Review review = createValidReview();

        when(reviewDAO.findById(reviewId))
                .thenReturn(review);

        Review result =
                reviewService.getReviewById(reviewId);

        assertNotNull(result);
        assertEquals(review, result);

        verify(reviewDAO, times(1))
                .findById(reviewId);
    }

    @Test
    void getReviewById_reviewNotFound() {

        long reviewId = 999L;

        when(reviewDAO.findById(reviewId))
                .thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> reviewService.getReviewById(reviewId)
                );

        assertEquals(
                "Review not found with ID: 999",
                exception.getMessage()
        );

        verify(reviewDAO, times(1))
                .findById(reviewId);
    }

    // =========================================================
    // GET ALL REVIEWS
    // =========================================================

    @Test
    void getAllReviews_success() {

        Review review1 = createValidReview();
        Review review2 = createValidReview();

        List<Review> reviews =
                Arrays.asList(review1, review2);

        when(reviewDAO.findAll())
                .thenReturn(reviews);

        List<Review> result =
                reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(reviews, result);

        verify(reviewDAO, times(1))
                .findAll();
    }

    @Test
    void getAllReviews_emptyList() {

        when(reviewDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Review> result =
                reviewService.getAllReviews();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(reviewDAO, times(1))
                .findAll();
    }

    // =========================================================
    // UPDATE REVIEW
    // =========================================================

    @Test
    void updateReview_success() {

        Review review = createValidReview();

        review.setReviewId(1L);

        when(reviewDAO.update(review))
                .thenReturn(true);

        boolean result =
                reviewService.updateReview(review);

        assertTrue(result);

        verify(reviewDAO, times(1))
                .update(review);
    }

    @Test
    void updateReview_notFound() {

        Review review = createValidReview();

        review.setReviewId(999L);

        when(reviewDAO.update(review))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Review not found with ID: 999",
                exception.getMessage()
        );

        verify(reviewDAO, times(1))
                .update(review);
    }

    // =========================================================
    // DELETE REVIEW
    // =========================================================

    @Test
    void deleteReview_success() {

        long reviewId = 1L;

        when(reviewDAO.delete(reviewId))
                .thenReturn(true);

        boolean result =
                reviewService.deleteReview(reviewId);

        assertTrue(result);

        verify(reviewDAO, times(1))
                .delete(reviewId);
    }

    @Test
    void deleteReview_notFound() {

        long reviewId = 999L;

        when(reviewDAO.delete(reviewId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> reviewService.deleteReview(reviewId)
                );

        assertEquals(
                "Review not found with ID: 999",
                exception.getMessage()
        );

        verify(reviewDAO, times(1))
                .delete(reviewId);
    }

    // =========================================================
    // CREATE VALIDATION EXCEPTIONS
    // =========================================================

    @Test
    void createReview_nullReview() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.createReview(null)
                );

        assertEquals(
                "Review cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void createReview_nullUser() {

        Review review = createValidReview();

        review.setUser(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.createReview(review)
                );

        assertEquals(
                "Review user cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void createReview_nullHotel() {

        Review review = createValidReview();

        review.setHotel(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.createReview(review)
                );

        assertEquals(
                "Review hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void createReview_ratingLessThanOne() {

        Review review = createValidReview();

        review.setRating(0);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.createReview(review)
                );

        assertEquals(
                "Review rating must be between 1 and 5",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void createReview_ratingGreaterThanFive() {

        Review review = createValidReview();

        review.setRating(6);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.createReview(review)
                );

        assertEquals(
                "Review rating must be between 1 and 5",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    // =========================================================
    // GET VALIDATION EXCEPTION
    // =========================================================

    @Test
    void getReviewById_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.getReviewById(0)
                );

        assertEquals(
                "Invalid review ID",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    // =========================================================
    // UPDATE VALIDATION EXCEPTIONS
    // =========================================================

    @Test
    void updateReview_nullReview() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(null)
                );

        assertEquals(
                "Review cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void updateReview_invalidId() {

        Review review = createValidReview();

        review.setReviewId(0L);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Invalid review ID",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void updateReview_nullUser() {

        Review review = createValidReview();

        review.setReviewId(1L);
        review.setUser(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Review user cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void updateReview_nullHotel() {

        Review review = createValidReview();

        review.setReviewId(1L);
        review.setHotel(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Review hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void updateReview_ratingLessThanOne() {

        Review review = createValidReview();

        review.setReviewId(1L);
        review.setRating(0);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Review rating must be between 1 and 5",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    @Test
    void updateReview_ratingGreaterThanFive() {

        Review review = createValidReview();

        review.setReviewId(1L);
        review.setRating(6);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.updateReview(review)
                );

        assertEquals(
                "Review rating must be between 1 and 5",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }

    // =========================================================
    // DELETE VALIDATION EXCEPTION
    // =========================================================

    @Test
    void deleteReview_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.deleteReview(0)
                );

        assertEquals(
                "Invalid review ID",
                exception.getMessage()
        );

        verifyNoInteractions(reviewDAO);
    }
}