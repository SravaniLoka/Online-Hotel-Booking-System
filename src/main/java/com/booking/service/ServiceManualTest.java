package com.booking.service;

import com.booking.model.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class ServiceManualTest {

    public static void main(String[] args) {

        System.out.println("========== SERVICE TEST START ==========");

        // Create service objects
        UserService userService = new UserServiceImpl();
        LocationService locationService = new LocationServiceImpl();
        HotelService hotelService = new HotelServiceImpl();
        RoomService roomService = new RoomServiceImpl();
        BookingService bookingService = new BookingServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();
        ReviewService reviewService = new ReviewServiceImpl();
        HotelImageService hotelImageService =
                new HotelImageServiceImpl();

        /*
         * IDs are stored so that the records can be deleted
         * later in reverse foreign-key order.
         */
        long userId;
        long locationId;
        long hotelId;
        long roomId;
        long bookingId;
        long paymentId;
        long reviewId;
        long imageId;

        // =========================================================
        // USER SERVICE
        // =========================================================

        System.out.println("\n--- USER SERVICE ---");

        User user = new User();

        user.setFullName("Service Test User");
        user.setEmail("service@test.com");
        user.setPasswordHash("test123");
        user.setPhone("9999999999");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        boolean userCreated = userService.createUser(user);

        System.out.println("CREATE: " + userCreated);

        userId = user.getUserId();

        System.out.println("Generated User ID: " + userId);

        User foundUser =
                userService.getUserById(userId);

        if (foundUser != null) {
            System.out.println("READ: " + foundUser.getFullName());
        }

        user.setFullName("Updated Service User");

        boolean userUpdated =
                userService.updateUser(user);

        System.out.println("UPDATE: " + userUpdated);


        // =========================================================
        // LOCATION SERVICE
        // =========================================================

        System.out.println("\n--- LOCATION SERVICE ---");

        Location location = new Location();

        location.setName("Hyderabad");
        location.setType("CITY");

        boolean locationCreated =
                locationService.createLocation(location);

        System.out.println("CREATE: " + locationCreated);

        locationId = location.getLocationId();

        System.out.println(
                "Generated Location ID: " + locationId
        );

        Location foundLocation =
                locationService.getLocationById(locationId);

        if (foundLocation != null) {
            System.out.println(
                    "READ: " + foundLocation.getName()
            );
        }

        location.setName("Hyderabad Updated");

        boolean locationUpdated =
                locationService.updateLocation(location);

        System.out.println("UPDATE: " + locationUpdated);


        // =========================================================
        // HOTEL SERVICE
        // =========================================================

        System.out.println("\n--- HOTEL SERVICE ---");

        Hotel hotel = new Hotel();

        hotel.setLocation(location);
        hotel.setName("Service Test Hotel");
        hotel.setDescription("Hotel created through service test");
        hotel.setAddress("Hyderabad");
        hotel.setStarRating(
                new BigDecimal("4.5")
        );
        hotel.setAmenities("WiFi, Pool, Parking");
        hotel.setStatus("ACTIVE");

        boolean hotelCreated =
                hotelService.createHotel(hotel);

        System.out.println("CREATE: " + hotelCreated);

        hotelId = hotel.getHotelId();

        System.out.println(
                "Generated Hotel ID: " + hotelId
        );

        Hotel foundHotel =
                hotelService.getHotelById(hotelId);

        if (foundHotel != null) {
            System.out.println(
                    "READ: " + foundHotel.getName()
            );
        }

        hotel.setName("Updated Service Hotel");

        boolean hotelUpdated =
                hotelService.updateHotel(hotel);

        System.out.println("UPDATE: " + hotelUpdated);


        // =========================================================
        // ROOM SERVICE
        // =========================================================

        System.out.println("\n--- ROOM SERVICE ---");

        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(
                new BigDecimal("2500.00")
        );
        room.setStatus("AVAILABLE");

        boolean roomCreated =
                roomService.createRoom(room);

        System.out.println("CREATE: " + roomCreated);

        roomId = room.getRoomId();

        System.out.println(
                "Generated Room ID: " + roomId
        );

        Room foundRoom =
                roomService.getRoomById(roomId);

        if (foundRoom != null) {
            System.out.println(
                    "READ: " + foundRoom.getRoomNumber()
            );
        }

        room.setRoomType("SUPER DELUXE");

        boolean roomUpdated =
                roomService.updateRoom(room);

        System.out.println("UPDATE: " + roomUpdated);


        // =========================================================
        // BOOKING SERVICE
        // =========================================================

        System.out.println("\n--- BOOKING SERVICE ---");

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckInDate(
                Date.valueOf("2026-10-01")
        );
        booking.setCheckOutDate(
                Date.valueOf("2026-10-03")
        );
        booking.setGuests(2);
        booking.setTotalAmount(
                new BigDecimal("5000.00")
        );
        booking.setBookingStatus("CONFIRMED");

        boolean bookingCreated =
                bookingService.createBooking(booking);

        System.out.println("CREATE: " + bookingCreated);

        bookingId = booking.getBookingId();

        System.out.println(
                "Generated Booking ID: " + bookingId
        );

        Booking foundBooking =
                bookingService.getBookingById(bookingId);

        if (foundBooking != null) {
            System.out.println(
                    "READ: " +
                            foundBooking.getBookingStatus()
            );
        }

        booking.setBookingStatus("CONFIRMED");

        boolean bookingUpdated =
                bookingService.updateBooking(booking);

        System.out.println("UPDATE: " + bookingUpdated);


        // =========================================================
        // PAYMENT SERVICE
        // =========================================================

        System.out.println("\n--- PAYMENT SERVICE ---");

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(
                new BigDecimal("5000.00")
        );
        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionRef(
                "SERVICE-TXN-001"
        );
        payment.setPaidAt(
                new Timestamp(System.currentTimeMillis())
        );

        boolean paymentCreated =
                paymentService.createPayment(payment);

        System.out.println("CREATE: " + paymentCreated);

        paymentId = payment.getPaymentId();

        System.out.println(
                "Generated Payment ID: " + paymentId
        );

        Payment foundPayment =
                paymentService.getPaymentById(paymentId);

        if (foundPayment != null) {
            System.out.println(
                    "READ: " +
                            foundPayment.getPaymentStatus()
            );
        }

        payment.setPaymentStatus("SUCCESS");

        boolean paymentUpdated =
                paymentService.updatePayment(payment);

        System.out.println("UPDATE: " + paymentUpdated);


        // =========================================================
        // REVIEW SERVICE
        // =========================================================

        System.out.println("\n--- REVIEW SERVICE ---");

        Review review = new Review();

        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(5);
        review.setComment("Excellent hotel!");

        boolean reviewCreated =
                reviewService.createReview(review);

        System.out.println("CREATE: " + reviewCreated);

        reviewId = review.getReviewId();

        System.out.println(
                "Generated Review ID: " + reviewId
        );

        Review foundReview =
                reviewService.getReviewById(reviewId);

        if (foundReview != null) {
            System.out.println(
                    "READ: " + foundReview.getComment()
            );
        }

        review.setComment("Excellent service!");

        boolean reviewUpdated =
                reviewService.updateReview(review);

        System.out.println("UPDATE: " + reviewUpdated);


        // =========================================================
        // HOTEL IMAGE SERVICE
        // =========================================================

        System.out.println("\n--- HOTEL IMAGE SERVICE ---");

        HotelImage hotelImage = new HotelImage();

        hotelImage.setHotel(hotel);
        hotelImage.setImageUrl(
                "https://example.com/hotel.jpg"
        );
        hotelImage.setCaption("Hotel exterior");

        boolean imageCreated =
                hotelImageService.createHotelImage(hotelImage);

        System.out.println("CREATE: " + imageCreated);

        imageId = hotelImage.getImageId();

        System.out.println(
                "Generated Image ID: " + imageId
        );

        HotelImage foundImage =
                hotelImageService.getHotelImageById(imageId);

        if (foundImage != null) {
            System.out.println(
                    "READ: " + foundImage.getCaption()
            );
        }

        hotelImage.setCaption("Updated hotel exterior");

        boolean imageUpdated =
                hotelImageService.updateHotelImage(hotelImage);

        System.out.println("UPDATE: " + imageUpdated);


        // =========================================================
        // DELETE TESTS
        // =========================================================

        System.out.println("\n========== DELETE TESTS ==========");

        boolean imageDeleted =
                hotelImageService.deleteHotelImage(imageId);

        System.out.println(
                "HotelImage DELETE: " + imageDeleted
        );

        boolean reviewDeleted =
                reviewService.deleteReview(reviewId);

        System.out.println(
                "Review DELETE: " + reviewDeleted
        );

        boolean paymentDeleted =
                paymentService.deletePayment(paymentId);

        System.out.println(
                "Payment DELETE: " + paymentDeleted
        );

        boolean bookingDeleted =
                bookingService.deleteBooking(bookingId);

        System.out.println(
                "Booking DELETE: " + bookingDeleted
        );

        boolean roomDeleted =
                roomService.deleteRoom(roomId);

        System.out.println(
                "Room DELETE: " + roomDeleted
        );

        boolean hotelDeleted =
                hotelService.deleteHotel(hotelId);

        System.out.println(
                "Hotel DELETE: " + hotelDeleted
        );

        boolean locationDeleted =
                locationService.deleteLocation(locationId);

        System.out.println(
                "Location DELETE: " + locationDeleted
        );

        boolean userDeleted =
                userService.deleteUser(userId);

        System.out.println(
                "User DELETE: " + userDeleted
        );

        System.out.println(
                "\n========== SERVICE TEST COMPLETE =========="
        );
    }
}