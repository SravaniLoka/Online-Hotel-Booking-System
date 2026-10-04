
        package com.booking;

import com.booking.Controller.BookingController;
import com.booking.Controller.HotelController;
import com.booking.Controller.HotelImageController;
import com.booking.Controller.LocationController;
import com.booking.Controller.PaymentController;
import com.booking.Controller.ReviewController;
import com.booking.Controller.RoomController;
import com.booking.Controller.UserController;
import com.booking.model.User;
import com.booking.menu.AccountMenu;
import com.booking.menu.BookingMenu;
import com.booking.menu.HotelImageMenu;
import com.booking.menu.HotelMenu;
import com.booking.menu.LocationMenu;
import com.booking.menu.MainMenu;
import com.booking.menu.PaymentMenu;
import com.booking.menu.ReviewMenu;
import com.booking.menu.RoomMenu;
import com.booking.menu.UserMenu;

public class Main {

    public static void main(String[] args) {

        // 1. Create the console scanner
        ConsoleScanner scanner = new ConsoleScanner();

        try {
            // 2. Create controllers
            UserController userController = new UserController();

            LocationController locationController =
                    new LocationController();

            HotelController hotelController =
                    new HotelController();

            RoomController roomController =
                    new RoomController();

            BookingController bookingController =
                    new BookingController();

            PaymentController paymentController =
                    new PaymentController();

            ReviewController reviewController =
                    new ReviewController();

            HotelImageController hotelImageController =
                    new HotelImageController();

            // 3. Create the authentication menu
            AccountMenu accountMenu =
                    new AccountMenu(userController, scanner);

            // 4. Create management menus
            UserMenu userMenu =
                    new UserMenu(userController, scanner);

            LocationMenu locationMenu =
                    new LocationMenu(
                            locationController,
                            scanner
                    );

            HotelMenu hotelMenu =
                    new HotelMenu(
                            hotelController,
                            locationController,
                            scanner
                    );

            RoomMenu roomMenu =
                    new RoomMenu(
                            roomController,
                            hotelController,
                            scanner
                    );

            BookingMenu bookingMenu =
                    new BookingMenu(
                            bookingController,
                            userController,
                            hotelController,
                            roomController,
                            scanner
                    );

            PaymentMenu paymentMenu =
                    new PaymentMenu(
                            paymentController,
                            bookingController,
                            scanner
                    );

            ReviewMenu reviewMenu =
                    new ReviewMenu(
                            reviewController,
                            userController,
                            hotelController,
                            scanner
                    );

            HotelImageMenu hotelImageMenu =
                    new HotelImageMenu(
                            hotelImageController,
                            hotelController,
                            scanner
                    );

            // 5. Create the main management menu
            MainMenu mainMenu =
                    new MainMenu(
                            userMenu,
                            locationMenu,
                            hotelMenu,
                            roomMenu,
                            bookingMenu,
                            paymentMenu,
                            reviewMenu,
                            hotelImageMenu,
                            scanner
                    );

            // 6. Authentication and application flow
            boolean running = true;

            while (running) {

                // Show Register / Login / Exit first
                User loggedInUser = accountMenu.start();
                // Exit was selected in the authentication menu
                if (loggedInUser == null) {
                    running = false;
                    continue;
                }

                // Login succeeded: open the management menu
                System.out.println();
                System.out.println(
                        "Welcome to the BB&J Hotel Booking System!"
                );

                mainMenu.start();

                // When the main menu returns, the loop shows
                // the authentication menu again.
                System.out.println();
                System.out.println(
                        "Returning to the authentication menu..."
                );
            }

        } finally {
            // 7. Close the console when the application exits
            scanner.close();
        }

        System.out.println("Application closed. Goodbye!");
    }
}
