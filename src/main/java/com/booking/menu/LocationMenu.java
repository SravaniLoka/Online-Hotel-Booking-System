
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.LocationController;
import com.booking.model.Location;

import java.util.List;

public class LocationMenu {

    private final LocationController locationController;
    private final ConsoleScanner scanner;

    public LocationMenu(
            LocationController locationController,
            ConsoleScanner scanner
    ) {
        this.locationController = locationController;
        this.scanner = scanner;
    }

    public void start() {
        boolean locationMenuRunning = true;

        while (locationMenuRunning) {
            System.out.println();
            System.out.println("----- LOCATION MANAGEMENT -----");
            System.out.println("1. Create Location");
            System.out.println("2. Find Location");
            System.out.println("3. View All Locations");
            System.out.println("4. Update Location");
            System.out.println("5. Delete Location");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createLocation();
                    break;

                case 2:
                    findLocation();
                    break;

                case 3:
                    viewAllLocations();
                    break;

                case 4:
                    updateLocation();
                    break;

                case 5:
                    deleteLocation();
                    break;

                case 6:
                    locationMenuRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createLocation() {
        System.out.println();
        System.out.println("----- CREATE LOCATION -----");

        System.out.print("Enter location name: ");
        String name = scanner.nextLine();

        System.out.print("Enter location type: ");
        String type = scanner.nextLine();

        System.out.print("Enter parent location ID (0 for none): ");
        long parentId = scanner.nextLong();
        scanner.nextLine();

        Location parent = null;

        if (parentId != 0) {
            parent = locationController.getLocationById(parentId);

            if (parent == null) {
                System.out.println("Parent location not found.");
                return;
            }
        }

        Location location = new Location();
        location.setName(name);
        location.setType(type);
        location.setParent(parent);

        boolean created = locationController.createLocation(location);

        if (created) {
            System.out.println(
                    "Location created successfully. ID: "
                            + location.getLocationId()
            );
        } else {
            System.out.println("Location creation failed.");
        }
    }

    private void findLocation() {
        System.out.println();
        System.out.println("----- FIND LOCATION -----");

        System.out.print("Enter Location ID: ");
        long locationId = scanner.nextLong();
        scanner.nextLine();

        Location location =
                locationController.getLocationById(locationId);

        if (location != null) {
            printLocation(location);
        } else {
            System.out.println("Location not found.");
        }
    }

    private void viewAllLocations() {
        System.out.println();
        System.out.println("----- ALL LOCATIONS -----");

        List<Location> locations =
                locationController.getAllLocations();

        if (locations == null || locations.isEmpty()) {
            System.out.println("No locations found.");
            return;
        }

        for (Location location : locations) {
            printLocation(location);
            System.out.println("------------------------------------");
        }
    }

    private void updateLocation() {
        System.out.println();
        System.out.println("----- UPDATE LOCATION -----");

        System.out.print("Enter Location ID: ");
        long locationId = scanner.nextLong();
        scanner.nextLine();

        Location location =
                locationController.getLocationById(locationId);

        if (location == null) {
            System.out.println("Location not found.");
            return;
        }

        System.out.print("Enter location name: ");
        location.setName(scanner.nextLine());

        System.out.print("Enter location type: ");
        location.setType(scanner.nextLine());

        System.out.print("Enter parent location ID (0 for none): ");
        long parentId = scanner.nextLong();
        scanner.nextLine();

        Location parent = null;

        if (parentId != 0) {
            parent = locationController.getLocationById(parentId);

            if (parent == null) {
                System.out.println("Parent location not found.");
                return;
            }
        }

        location.setParent(parent);

        boolean updated =
                locationController.updateLocation(location);

        System.out.println(
                updated
                        ? "Location updated successfully."
                        : "Location update failed."
        );
    }

    private void deleteLocation() {
        System.out.println();
        System.out.println("----- DELETE LOCATION -----");

        System.out.print("Enter Location ID: ");
        long locationId = scanner.nextLong();

        Location location =
                locationController.getLocationById(locationId);

        if (location == null) {
            System.out.println("Location not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this location? (yes/no): "
        );

        scanner.nextLine();
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean deleted =
                    locationController.deleteLocation(locationId);

            System.out.println(
                    deleted
                            ? "Location deleted successfully."
                            : "Location deletion failed."
            );
        } else {
            System.out.println("Location deletion cancelled.");
        }
    }

    private void printLocation(Location location) {
        System.out.println("Location ID: " + location.getLocationId());
        System.out.println("Name: " + location.getName());
        System.out.println("Type: " + location.getType());

        if (location.getParent() != null) {
            System.out.println(
                    "Parent Location ID: "
                            + location.getParent().getLocationId()
            );

            System.out.println(
                    "Parent Location Name: "
                            + location.getParent().getName()
            );
        } else {
            System.out.println("Parent Location: None");
        }
    }
}
