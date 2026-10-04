
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.HotelController;
import com.booking.Controller.LocationController;
import com.booking.model.Hotel;
import com.booking.model.Location;

import java.math.BigDecimal;
import java.util.List;

public class HotelMenu {

    private final HotelController hotelController;
    private final LocationController locationController;
    private final ConsoleScanner scanner;

    public HotelMenu(
            HotelController hotelController,
            LocationController locationController,
            ConsoleScanner scanner
    ) {
        this.hotelController = hotelController;
        this.locationController = locationController;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("----- HOTEL MANAGEMENT -----");
            System.out.println("1. Create Hotel");
            System.out.println("2. Find Hotel");
            System.out.println("3. View All Hotels");
            System.out.println("4. Update Hotel");
            System.out.println("5. Delete Hotel");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createHotel();
                    break;

                case 2:
                    findHotel();
                    break;

                case 3:
                    viewAllHotels();
                    break;

                case 4:
                    updateHotel();
                    break;

                case 5:
                    deleteHotel();
                    break;

                case 6:
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createHotel() {
        System.out.println();
        System.out.println("----- CREATE HOTEL -----");

        System.out.print("Enter Location ID: ");
        long locationId = scanner.nextLong();
        scanner.nextLine();

        Location location =
                locationController.getLocationById(locationId);

        if (location == null) {
            System.out.println("Location not found.");
            return;
        }

        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter star rating: ");
        BigDecimal starRating =
                new BigDecimal(scanner.nextLine());

        System.out.print("Enter amenities: ");
        String amenities = scanner.nextLine();

        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        Hotel hotel = new Hotel();
        hotel.setLocation(location);
        hotel.setName(name);
        hotel.setDescription(description);
        hotel.setAddress(address);
        hotel.setStarRating(starRating);
        hotel.setAmenities(amenities);
        hotel.setStatus(status);

        boolean created = hotelController.createHotel(hotel);

        if (created) {
            System.out.println(
                    "Hotel created successfully. ID: "
                            + hotel.getHotelId()
            );
        } else {
            System.out.println("Hotel creation failed.");
        }
    }

    private void findHotel() {
        System.out.println();
        System.out.println("----- FIND HOTEL -----");

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel != null) {
            printHotel(hotel);
        } else {
            System.out.println("Hotel not found.");
        }
    }

    private void viewAllHotels() {
        System.out.println();
        System.out.println("----- ALL HOTELS -----");

        List<Hotel> hotels = hotelController.getAllHotels();

        if (hotels == null || hotels.isEmpty()) {
            System.out.println("No hotels found.");
            return;
        }

        for (Hotel hotel : hotels) {
            printHotel(hotel);
            System.out.println("------------------------------------");
        }
    }

    private void updateHotel() {
        System.out.println();
        System.out.println("----- UPDATE HOTEL -----");

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print("Enter Location ID: ");
        long locationId = scanner.nextLong();
        scanner.nextLine();

        Location location =
                locationController.getLocationById(locationId);

        if (location == null) {
            System.out.println("Location not found.");
            return;
        }

        hotel.setLocation(location);

        System.out.print("Enter hotel name: ");
        hotel.setName(scanner.nextLine());

        System.out.print("Enter description: ");
        hotel.setDescription(scanner.nextLine());

        System.out.print("Enter address: ");
        hotel.setAddress(scanner.nextLine());

        System.out.print("Enter star rating: ");
        hotel.setStarRating(
                new BigDecimal(scanner.nextLine())
        );

        System.out.print("Enter amenities: ");
        hotel.setAmenities(scanner.nextLine());

        System.out.print("Enter status: ");
        hotel.setStatus(scanner.nextLine());

        boolean updated = hotelController.updateHotel(hotel);

        System.out.println(
                updated
                        ? "Hotel updated successfully."
                        : "Hotel update failed."
        );
    }

    private void deleteHotel() {
        System.out.println();
        System.out.println("----- DELETE HOTEL -----");

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this hotel? (yes/no): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean deleted = hotelController.deleteHotel(hotelId);

            System.out.println(
                    deleted
                            ? "Hotel deleted successfully."
                            : "Hotel deletion failed."
            );
        } else {
            System.out.println("Hotel deletion cancelled.");
        }
    }

    private void printHotel(Hotel hotel) {
        System.out.println("Hotel ID: " + hotel.getHotelId());

        if (hotel.getLocation() != null) {
            System.out.println(
                    "Location ID: "
                            + hotel.getLocation().getLocationId()
            );

            System.out.println(
                    "Location Name: "
                            + hotel.getLocation().getName()
            );
        } else {
            System.out.println("Location: None");
        }

        System.out.println("Name: " + hotel.getName());
        System.out.println("Description: " + hotel.getDescription());
        System.out.println("Address: " + hotel.getAddress());
        System.out.println("Star Rating: " + hotel.getStarRating());
        System.out.println("Amenities: " + hotel.getAmenities());
        System.out.println("Status: " + hotel.getStatus());
    }
}