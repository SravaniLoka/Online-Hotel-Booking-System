
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.HotelController;
import com.booking.Controller.ReviewController;
import com.booking.Controller.UserController;
import com.booking.model.Hotel;
import com.booking.model.Review;
import com.booking.model.User;

import java.util.List;

public class ReviewMenu {

    private final ReviewController reviewController;
    private final UserController userController;
    private final HotelController hotelController;
    private final ConsoleScanner scanner;

    public ReviewMenu(
            ReviewController reviewController,
            UserController userController,
            HotelController hotelController,
            ConsoleScanner scanner) {

        this.reviewController = reviewController;
        this.userController = userController;
        this.hotelController = hotelController;
        this.scanner = scanner;
    }

    public void start() {

        boolean reviewMenuRunning = true;

        while (reviewMenuRunning) {

            System.out.println();
            System.out.println("----- REVIEW MANAGEMENT -----");
            System.out.println("1. Create Review");
            System.out.println("2. Find Review");
            System.out.println("3. View All Reviews");
            System.out.println("4. Update Review");
            System.out.println("5. Delete Review");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createReview();
                    break;

                case 2:
                    findReview();
                    break;

                case 3:
                    viewAllReviews();
                    break;

                case 4:
                    updateReview();
                    break;

                case 5:
                    deleteReview();
                    break;

                case 6:
                    reviewMenuRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createReview() {

        System.out.println();
        System.out.println("----- CREATE REVIEW -----");

        System.out.print("Enter User ID: ");
        long userId = scanner.nextLong();

        User user = userController.getUserById(userId);

        if (user == null) {
            System.out.println("User not found.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter comment: ");
        String comment = scanner.nextLine();

        Review review = new Review();

        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(rating);
        review.setComment(comment);

        boolean created = reviewController.createReview(review);

        if (created) {
            System.out.println(
                    "Review created successfully. ID: "
                            + review.getReviewId()
            );
        } else {
            System.out.println("Review creation failed.");
        }
    }

    private void findReview() {

        System.out.println();
        System.out.println("----- FIND REVIEW -----");

        System.out.print("Enter Review ID: ");
        long reviewId = scanner.nextLong();

        Review review =
                reviewController.getReviewById(reviewId);

        if (review != null) {
            printReview(review);
        } else {
            System.out.println("Review not found.");
        }
    }

    private void viewAllReviews() {

        System.out.println();
        System.out.println("----- ALL REVIEWS -----");

        List<Review> reviews =
                reviewController.getAllReviews();

        if (reviews == null || reviews.isEmpty()) {
            System.out.println("No reviews found.");
            return;
        }

        for (Review review : reviews) {
            printReview(review);
            System.out.println("------------------------------------");
        }
    }

    private void updateReview() {

        System.out.println();
        System.out.println("----- UPDATE REVIEW -----");

        System.out.print("Enter Review ID: ");
        long reviewId = scanner.nextLong();

        Review review =
                reviewController.getReviewById(reviewId);

        if (review == null) {
            System.out.println("Review not found.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter User ID: ");
        long userId = scanner.nextLong();

        User user = userController.getUserById(userId);

        if (user == null) {
            System.out.println("User not found.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print("Enter rating (1-5): ");
        review.setRating(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter comment: ");
        review.setComment(scanner.nextLine());

        review.setUser(user);
        review.setHotel(hotel);

        boolean updated = reviewController.updateReview(review);

        System.out.println(
                updated
                        ? "Review updated successfully."
                        : "Review update failed."
        );
    }

    private void deleteReview() {

        System.out.println();
        System.out.println("----- DELETE REVIEW -----");

        System.out.print("Enter Review ID: ");
        long reviewId = scanner.nextLong();

        Review review =
                reviewController.getReviewById(reviewId);

        if (review == null) {
            System.out.println("Review not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this review? (yes/no): "
        );

        scanner.nextLine();
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {

            boolean deleted =
                    reviewController.deleteReview(reviewId);

            System.out.println(
                    deleted
                            ? "Review deleted successfully."
                            : "Review deletion failed."
            );

        } else {
            System.out.println("Review deletion cancelled.");
        }
    }

    private void printReview(Review review) {

        System.out.println("Review ID: " + review.getReviewId());

        if (review.getUser() != null) {
            System.out.println(
                    "User ID: " + review.getUser().getUserId()
            );
            System.out.println(
                    "User Name: " + review.getUser().getFullName()
            );
        } else {
            System.out.println("User: None");
        }

        if (review.getHotel() != null) {
            System.out.println(
                    "Hotel ID: " + review.getHotel().getHotelId()
            );
            System.out.println(
                    "Hotel Name: " + review.getHotel().getName()
            );
        } else {
            System.out.println("Hotel: None");
        }

        System.out.println("Rating: " + review.getRating());
        System.out.println("Comment: " + review.getComment());
    }
}
