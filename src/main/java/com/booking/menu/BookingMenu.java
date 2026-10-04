
package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.BookingController;
import com.booking.Controller.HotelController;
import com.booking.Controller.RoomController;
import com.booking.Controller.UserController;
import com.booking.model.Booking;
import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.model.User;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class BookingMenu {

    private final BookingController bookingController;
    private final UserController userController;
    private final HotelController hotelController;
    private final RoomController roomController;
    private final ConsoleScanner scanner;

    public BookingMenu(
            BookingController bookingController,
            UserController userController,
            HotelController hotelController,
            RoomController roomController,
            ConsoleScanner scanner) {

        this.bookingController = bookingController;
        this.userController = userController;
        this.hotelController = hotelController;
        this.roomController = roomController;
        this.scanner = scanner;
    }

    public void start() {

        boolean bookingMenuRunning = true;

        while (bookingMenuRunning) {

            System.out.println();
            System.out.println("----- BOOKING MANAGEMENT -----");
            System.out.println("1. Create Booking");
            System.out.println("2. Find Booking");
            System.out.println("3. View All Bookings");
            System.out.println("4. Update Booking");
            System.out.println("5. Delete Booking");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createBooking();
                    break;

                case 2:
                    findBooking();
                    break;

                case 3:
                    viewAllBookings();
                    break;

                case 4:
                    updateBooking();
                    break;

                case 5:
                    deleteBooking();
                    break;

                case 6:
                    bookingMenuRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createBooking() {

        System.out.println();
        System.out.println("----- CREATE BOOKING -----");

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

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Room ID: ");
        long roomId = scanner.nextLong();
        scanner.nextLine();

        Room room = roomController.getRoomById(roomId);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.print(
                "Enter check-in date (yyyy-[m]m-[d]d): "
        );
        Date checkInDate = Date.valueOf(scanner.nextLine());

        System.out.print(
                "Enter check-out date (yyyy-[m]m-[d]d): "
        );
        Date checkOutDate = Date.valueOf(scanner.nextLine());

        System.out.print("Enter number of guests: ");
        int guests = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter total amount: ");
        BigDecimal totalAmount =
                new BigDecimal(scanner.nextLine());

        // Ask the user whether to proceed with payment.
        System.out.print(
                "Do you want to proceed with payment? (yes/no): "
        );
        String paymentChoice = scanner.nextLine().trim();

        if (paymentChoice.equalsIgnoreCase("no")) {
            System.out.println(
                    "Payment is required to complete the booking."
            );
            System.out.println(
                    "Booking has not been created."
            );
            return;
        }

        if (!paymentChoice.equalsIgnoreCase("yes")) {
            System.out.println(
                    "Invalid choice. Please enter yes or no."
            );
            return;
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setGuests(guests);
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus("PENDING");

        boolean created =
                bookingController.createBooking(booking);

        if (created) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("       ROOM BOOKED SUCCESSFULLY!");
            System.out.println("========================================");

            printBooking(booking);

            System.out.println("========================================");
            System.out.println(
                    "Booking record created successfully."
            );
            System.out.println(
                    "Complete payment to finalize your booking."
            );

        } else {
            System.out.println("Booking creation failed.");
        }
    }

    private void findBooking() {

        System.out.println();
        System.out.println("----- FIND BOOKING -----");

        System.out.print("Enter Booking ID: ");
        long bookingId = scanner.nextLong();

        Booking booking =
                bookingController.getBookingById(bookingId);

        if (booking != null) {
            printBooking(booking);
        } else {
            System.out.println("Booking not found.");
        }
    }

    private void viewAllBookings() {

        System.out.println();
        System.out.println("----- ALL BOOKINGS -----");

        List<Booking> bookings =
                bookingController.getAllBookings();

        if (bookings == null || bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking booking : bookings) {
            printBooking(booking);
            System.out.println("------------------------------------");
        }
    }

    private void updateBooking() {

        System.out.println();
        System.out.println("----- UPDATE BOOKING -----");

        System.out.print("Enter Booking ID: ");
        long bookingId = scanner.nextLong();

        Booking booking =
                bookingController.getBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
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

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Room ID: ");
        long roomId = scanner.nextLong();
        scanner.nextLine();

        Room room = roomController.getRoomById(roomId);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.print(
                "Enter check-in date (yyyy-[m]m-[d]d): "
        );
        booking.setCheckInDate(
                Date.valueOf(scanner.nextLine())
        );

        System.out.print(
                "Enter check-out date (yyyy-[m]m-[d]d): "
        );
        booking.setCheckOutDate(
                Date.valueOf(scanner.nextLine())
        );

        System.out.print("Enter number of guests: ");
        booking.setGuests(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter total amount: ");
        booking.setTotalAmount(
                new BigDecimal(scanner.nextLine())
        );

        System.out.print("Enter booking status: ");
        booking.setBookingStatus(scanner.nextLine());

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);

        boolean updated =
                bookingController.updateBooking(booking);

        System.out.println(
                updated
                        ? "Booking updated successfully."
                        : "Booking update failed."
        );
    }

    private void deleteBooking() {

        System.out.println();
        System.out.println("----- DELETE BOOKING -----");

        System.out.print("Enter Booking ID: ");
        long bookingId = scanner.nextLong();

        Booking booking =
                bookingController.getBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this booking? (yes/no): "
        );

        scanner.nextLine();
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {

            boolean deleted =
                    bookingController.deleteBooking(bookingId);

            System.out.println(
                    deleted
                            ? "Booking deleted successfully."
                            : "Booking deletion failed."
            );

        } else {
            System.out.println("Booking deletion cancelled.");
        }
    }

    private void printBooking(Booking booking) {

        System.out.println(
                "Booking ID: " + booking.getBookingId()
        );

        if (booking.getUser() != null) {
            System.out.println(
                    "User ID: " + booking.getUser().getUserId()
            );
            System.out.println(
                    "User Name: " + booking.getUser().getFullName()
            );
        } else {
            System.out.println("User: None");
        }

        if (booking.getHotel() != null) {
            System.out.println(
                    "Hotel ID: " + booking.getHotel().getHotelId()
            );
            System.out.println(
                    "Hotel Name: " + booking.getHotel().getName()
            );
        } else {
            System.out.println("Hotel: None");
        }

        if (booking.getRoom() != null) {
            System.out.println(
                    "Room ID: " + booking.getRoom().getRoomId()
            );
            System.out.println(
                    "Room Number: " + booking.getRoom().getRoomNumber()
            );
        } else {
            System.out.println("Room: None");
        }

        System.out.println(
                "Check-in Date: " + booking.getCheckInDate()
        );
        System.out.println(
                "Check-out Date: " + booking.getCheckOutDate()
        );
        System.out.println("Guests: " + booking.getGuests());
        System.out.println(
                "Total Amount: " + booking.getTotalAmount()
        );
        System.out.println(
                "Booking Status: " + booking.getBookingStatus()
        );
    }
}
