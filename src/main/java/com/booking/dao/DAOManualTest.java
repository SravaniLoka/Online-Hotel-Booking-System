package com.booking.dao;

import com.booking.model.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class DAOManualTest {

    public static void main(String[] args) {

        System.out.println("========== DAO TEST START ==========");

        // =========================================================
        // 1. USER DAO
        // =========================================================

        UserDAO userDAO = new UserDAOImpl();

        User user = new User();
        user.setFullName("DAO Test User");
        user.setEmail("daotest123@gmail.com");
        user.setPasswordHash("test123");
        user.setPhone("9999999999");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        System.out.println("\n--- USER DAO ---");

        boolean userCreated = userDAO.create(user);
        System.out.println("CREATE: " + userCreated);
        System.out.println("Generated User ID: " + user.getUserId());

        User foundUser = userDAO.findById(user.getUserId());
        System.out.println("READ: " +
                (foundUser != null ? foundUser.getFullName() : "Not Found"));

        foundUser.setFullName("Updated DAO User");
        boolean userUpdated = userDAO.update(foundUser);
        System.out.println("UPDATE: " + userUpdated);

        // =========================================================
        // 2. LOCATION DAO
        // =========================================================

        LocationDAO locationDAO = new LocationDAOImpl();

        Location location = new Location();
        location.setName("Hyderabad");
        location.setType("CITY");
        location.setParent(null);

        System.out.println("\n--- LOCATION DAO ---");

        boolean locationCreated = locationDAO.create(location);
        System.out.println("CREATE: " + locationCreated);
        System.out.println("Generated Location ID: " + location.getLocationId());

        Location foundLocation =
                locationDAO.findById(location.getLocationId());

        System.out.println("READ: " +
                (foundLocation != null ? foundLocation.getName() : "Not Found"));

        foundLocation.setName("Hyderabad City");

        boolean locationUpdated = locationDAO.update(foundLocation);
        System.out.println("UPDATE: " + locationUpdated);

        // =========================================================
        // 3. HOTEL DAO
        // =========================================================

        HotelDAO hotelDAO = new HotelDAOImpl();

        Hotel hotel = new Hotel();
        hotel.setLocation(location);
        hotel.setName("DAO Test Hotel");
        hotel.setDescription("Hotel created for DAO testing");
        hotel.setAddress("Hyderabad");
        hotel.setStarRating(new BigDecimal("4.5"));
        hotel.setAmenities("WiFi, AC, Parking");
        hotel.setStatus("ACTIVE");

        System.out.println("\n--- HOTEL DAO ---");

        boolean hotelCreated = hotelDAO.create(hotel);
        System.out.println("CREATE: " + hotelCreated);
        System.out.println("Generated Hotel ID: " + hotel.getHotelId());

        Hotel foundHotel = hotelDAO.findById(hotel.getHotelId());

        System.out.println("READ: " +
                (foundHotel != null ? foundHotel.getName() : "Not Found"));

        foundHotel.setName("Updated DAO Hotel");

        boolean hotelUpdated = hotelDAO.update(foundHotel);
        System.out.println("UPDATE: " + hotelUpdated);

        // =========================================================
        // 4. ROOM DAO
        // =========================================================

        RoomDAO roomDAO = new RoomDAOImpl();

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        System.out.println("\n--- ROOM DAO ---");

        boolean roomCreated = roomDAO.create(room);
        System.out.println("CREATE: " + roomCreated);
        System.out.println("Generated Room ID: " + room.getRoomId());

        Room foundRoom = roomDAO.findById(room.getRoomId());

        System.out.println("READ: " +
                (foundRoom != null ? foundRoom.getRoomNumber() : "Not Found"));

        foundRoom.setBasePrice(new BigDecimal("3000.00"));

        boolean roomUpdated = roomDAO.update(foundRoom);
        System.out.println("UPDATE: " + roomUpdated);

        // =========================================================
        // 5. BOOKING DAO
        // =========================================================

        BookingDAO bookingDAO = new BookingDAOImpl();

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);

        booking.setCheckInDate(Date.valueOf("2026-10-01"));
        booking.setCheckOutDate(Date.valueOf("2026-10-03"));

        booking.setGuests(2);
        booking.setTotalAmount(new BigDecimal("6000.00"));
        booking.setBookingStatus("CONFIRMED");

        System.out.println("\n--- BOOKING DAO ---");

        boolean bookingCreated = bookingDAO.create(booking);
        System.out.println("CREATE: " + bookingCreated);
        System.out.println("Generated Booking ID: " + booking.getBookingId());

        Booking foundBooking =
                bookingDAO.findById(booking.getBookingId());

        System.out.println("READ: " +
                (foundBooking != null
                        ? foundBooking.getBookingStatus()
                        : "Not Found"));

        foundBooking.setBookingStatus("CANCELLED");

        boolean bookingUpdated = bookingDAO.update(foundBooking);
        System.out.println("UPDATE: " + bookingUpdated);

        // =========================================================
        // 6. PAYMENT DAO
        // =========================================================

        PaymentDAO paymentDAO = new PaymentDAOImpl();

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(new BigDecimal("6000.00"));
        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionRef("DAO-TEST-001");
        payment.setPaidAt(new Timestamp(System.currentTimeMillis()));

        System.out.println("\n--- PAYMENT DAO ---");

        boolean paymentCreated = paymentDAO.create(payment);
        System.out.println("CREATE: " + paymentCreated);
        System.out.println("Generated Payment ID: " + payment.getPaymentId());

        Payment foundPayment =
                paymentDAO.findById(payment.getPaymentId());

        System.out.println("READ: " +
                (foundPayment != null
                        ? foundPayment.getPaymentStatus()
                        : "Not Found"));

        foundPayment.setPaymentStatus("REFUNDED");

        boolean paymentUpdated = paymentDAO.update(foundPayment);
        System.out.println("UPDATE: " + paymentUpdated);

        // =========================================================
        // 7. REVIEW DAO
        // =========================================================

        ReviewDAO reviewDAO = new ReviewDAOImpl();

        Review review = new Review();

        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(5);
        review.setComment("Excellent hotel!");

        System.out.println("\n--- REVIEW DAO ---");

        boolean reviewCreated = reviewDAO.create(review);
        System.out.println("CREATE: " + reviewCreated);
        System.out.println("Generated Review ID: " + review.getReviewId());

        Review foundReview =
                reviewDAO.findById(review.getReviewId());

        System.out.println("READ: " +
                (foundReview != null
                        ? foundReview.getComment()
                        : "Not Found"));

        foundReview.setRating(4);

        boolean reviewUpdated = reviewDAO.update(foundReview);
        System.out.println("UPDATE: " + reviewUpdated);

        // =========================================================
        // 8. HOTEL IMAGE DAO
        // =========================================================

        HotelImageDAO hotelImageDAO = new HotelImageDAOImpl();

        HotelImage image = new HotelImage();

        image.setHotel(hotel);
        image.setImageUrl("https://example.com/hotel.jpg");
        image.setCaption("Hotel exterior");

        System.out.println("\n--- HOTEL IMAGE DAO ---");

        boolean imageCreated = hotelImageDAO.create(image);
        System.out.println("CREATE: " + imageCreated);
        System.out.println("Generated Image ID: " + image.getImageId());

        HotelImage foundImage =
                hotelImageDAO.findById(image.getImageId());

        System.out.println("READ: " +
                (foundImage != null
                        ? foundImage.getCaption()
                        : "Not Found"));

        foundImage.setCaption("Updated hotel exterior");

        boolean imageUpdated = hotelImageDAO.update(foundImage);
        System.out.println("UPDATE: " + imageUpdated);

        // =========================================================
        // DELETE
        // =========================================================

        System.out.println("\n========== DELETE TESTS ==========");

        System.out.println("HotelImage DELETE: " +
                hotelImageDAO.delete(image.getImageId()));

        System.out.println("Review DELETE: " +
                reviewDAO.delete(review.getReviewId()));

        System.out.println("Payment DELETE: " +
                paymentDAO.delete(payment.getPaymentId()));

        System.out.println("Booking DELETE: " +
                bookingDAO.delete(booking.getBookingId()));

        System.out.println("Room DELETE: " +
                roomDAO.delete(room.getRoomId()));

        System.out.println("Hotel DELETE: " +
                hotelDAO.delete(hotel.getHotelId()));

        System.out.println("Location DELETE: " +
                locationDAO.delete(location.getLocationId()));

        System.out.println("User DELETE: " +
                userDAO.delete(user.getUserId()));

        System.out.println("\n========== DAO TEST COMPLETE ==========");
    }
}