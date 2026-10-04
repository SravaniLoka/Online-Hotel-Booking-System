
package com.booking.menu;

import com.booking.ConsoleScanner;

public class MainMenu {

    private final UserMenu userMenu;
    private final LocationMenu locationMenu;
    private final HotelMenu hotelMenu;
    private final RoomMenu roomMenu;
    private final BookingMenu bookingMenu;
    private final PaymentMenu paymentMenu;
    private final ReviewMenu reviewMenu;
    private final HotelImageMenu hotelImageMenu;
    private final ConsoleScanner scanner;

    public MainMenu(
            UserMenu userMenu,
            LocationMenu locationMenu,
            HotelMenu hotelMenu,
            RoomMenu roomMenu,
            BookingMenu bookingMenu,
            PaymentMenu paymentMenu,
            ReviewMenu reviewMenu,
            HotelImageMenu hotelImageMenu,
            ConsoleScanner scanner) {

        this.userMenu = userMenu;
        this.locationMenu = locationMenu;
        this.hotelMenu = hotelMenu;
        this.roomMenu = roomMenu;
        this.bookingMenu = bookingMenu;
        this.paymentMenu = paymentMenu;
        this.reviewMenu = reviewMenu;
        this.hotelImageMenu = hotelImageMenu;
        this.scanner = scanner;
    }

    public void start() {

        boolean running = true;

        System.out.println("====================================");
        System.out.println("       BB&J HOTEL BOOKING SYSTEM");
        System.out.println("====================================");

        while (running) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("              MAIN MENU");
            System.out.println("====================================");
            System.out.println("1. User Management");
            System.out.println("2. Location Management");
            System.out.println("3. Hotel Management");
            System.out.println("4. Room Management");
            System.out.println("5. Booking Management");
            System.out.println("6. Payment Management");
            System.out.println("7. Review Management");
            System.out.println("8. Hotel Image Management");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    userMenu.start();
                    break;

                case 2:
                    locationMenu.start();
                    break;

                case 3:
                    hotelMenu.start();
                    break;

                case 4:
                    roomMenu.start();
                    break;

                case 5:
                    bookingMenu.start();
                    break;

                case 6:
                    paymentMenu.start();
                    break;

                case 7:
                    reviewMenu.start();
                    break;

                case 8:
                    hotelImageMenu.start();
                    break;

                case 9:
                    running = false;
                    System.out.println();
                    System.out.println(
                            "Exiting BB&J Hotel Booking System..."
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }
}
