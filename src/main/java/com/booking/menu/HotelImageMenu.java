
package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.HotelController;
import com.booking.Controller.HotelImageController;
import com.booking.model.Hotel;
import com.booking.model.HotelImage;

import java.util.List;

public class HotelImageMenu {

    private final HotelImageController hotelImageController;
    private final HotelController hotelController;
    private final ConsoleScanner scanner;

    public HotelImageMenu(
            HotelImageController hotelImageController,
            HotelController hotelController,
            ConsoleScanner scanner) {

        this.hotelImageController = hotelImageController;
        this.hotelController = hotelController;
        this.scanner = scanner;
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("----- HOTEL IMAGE MANAGEMENT -----");
            System.out.println("1. Create Hotel Image");
            System.out.println("2. Find Hotel Image");
            System.out.println("3. View All Hotel Images");
            System.out.println("4. Update Hotel Image");
            System.out.println("5. Delete Hotel Image");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createHotelImage();
                    break;

                case 2:
                    findHotelImage();
                    break;

                case 3:
                    viewAllHotelImages();
                    break;

                case 4:
                    updateHotelImage();
                    break;

                case 5:
                    deleteHotelImage();
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

    private void createHotelImage() {

        System.out.println();
        System.out.println("----- CREATE HOTEL IMAGE -----");

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print("Enter image URL: ");
        String imageUrl = scanner.nextLine();

        System.out.print("Enter caption: ");
        String caption = scanner.nextLine();

        HotelImage hotelImage = new HotelImage();
        hotelImage.setHotel(hotel);
        hotelImage.setImageUrl(imageUrl);
        hotelImage.setCaption(caption);

        boolean created =
                hotelImageController.createHotelImage(hotelImage);

        if (created) {
            System.out.println(
                    "Hotel image created successfully. ID: "
                            + hotelImage.getImageId()
            );
        } else {
            System.out.println("Hotel image creation failed.");
        }
    }

    private void findHotelImage() {

        System.out.println();
        System.out.println("----- FIND HOTEL IMAGE -----");

        System.out.print("Enter Image ID: ");
        long imageId = scanner.nextLong();
        scanner.nextLine();

        HotelImage hotelImage =
                hotelImageController.getHotelImageById(imageId);

        if (hotelImage != null) {
            printHotelImage(hotelImage);
        } else {
            System.out.println("Hotel image not found.");
        }
    }

    private void viewAllHotelImages() {

        System.out.println();
        System.out.println("----- ALL HOTEL IMAGES -----");

        List<HotelImage> hotelImages =
                hotelImageController.getAllHotelImages();

        if (hotelImages == null || hotelImages.isEmpty()) {
            System.out.println("No hotel images found.");
            return;
        }

        for (HotelImage hotelImage : hotelImages) {
            printHotelImage(hotelImage);
            System.out.println(
                    "------------------------------------"
            );
        }
    }

    private void updateHotelImage() {

        System.out.println();
        System.out.println("----- UPDATE HOTEL IMAGE -----");

        System.out.print("Enter Image ID: ");
        long imageId = scanner.nextLong();
        scanner.nextLine();

        HotelImage hotelImage =
                hotelImageController.getHotelImageById(imageId);

        if (hotelImage == null) {
            System.out.println("Hotel image not found.");
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

        hotelImage.setHotel(hotel);

        System.out.print("Enter image URL: ");
        hotelImage.setImageUrl(scanner.nextLine());

        System.out.print("Enter caption: ");
        hotelImage.setCaption(scanner.nextLine());

        boolean updated =
                hotelImageController.updateHotelImage(hotelImage);

        System.out.println(
                updated
                        ? "Hotel image updated successfully."
                        : "Hotel image update failed."
        );
    }

    private void deleteHotelImage() {

        System.out.println();
        System.out.println("----- DELETE HOTEL IMAGE -----");

        System.out.print("Enter Image ID: ");
        long imageId = scanner.nextLong();
        scanner.nextLine();

        HotelImage hotelImage =
                hotelImageController.getHotelImageById(imageId);

        if (hotelImage == null) {
            System.out.println("Hotel image not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this hotel image? (yes/no): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {

            boolean deleted =
                    hotelImageController.deleteHotelImage(imageId);

            System.out.println(
                    deleted
                            ? "Hotel image deleted successfully."
                            : "Hotel image deletion failed."
            );

        } else {
            System.out.println("Hotel image deletion cancelled.");
        }
    }

    private void printHotelImage(HotelImage hotelImage) {

        System.out.println("Image ID: " + hotelImage.getImageId());

        if (hotelImage.getHotel() != null) {
            System.out.println(
                    "Hotel ID: "
                            + hotelImage.getHotel().getHotelId()
            );

            System.out.println(
                    "Hotel Name: "
                            + hotelImage.getHotel().getName()
            );
        } else {
            System.out.println("Hotel: None");
        }

        System.out.println("Image URL: " + hotelImage.getImageUrl());
        System.out.println("Caption: " + hotelImage.getCaption());
    }
}
