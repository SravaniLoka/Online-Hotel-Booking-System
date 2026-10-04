
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.HotelController;
import com.booking.Controller.RoomController;
import com.booking.model.Hotel;
import com.booking.model.Room;

import java.math.BigDecimal;
import java.util.List;

public class RoomMenu {

    private final RoomController roomController;
    private final HotelController hotelController;
    private final ConsoleScanner scanner;

    public RoomMenu(
            RoomController roomController,
            HotelController hotelController,
            ConsoleScanner scanner
    ) {
        this.roomController = roomController;
        this.hotelController = hotelController;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("----- ROOM MANAGEMENT -----");
            System.out.println("1. Create Room");
            System.out.println("2. Find Room");
            System.out.println("3. View All Rooms");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createRoom();
                    break;

                case 2:
                    findRoom();
                    break;

                case 3:
                    viewAllRooms();
                    break;

                case 4:
                    updateRoom();
                    break;

                case 5:
                    deleteRoom();
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

    private void createRoom() {
        System.out.println();
        System.out.println("----- CREATE ROOM -----");

        System.out.print("Enter Hotel ID: ");
        long hotelId = scanner.nextLong();
        scanner.nextLine();

        Hotel hotel = hotelController.getHotelById(hotelId);

        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();

        System.out.print("Enter room type: ");
        String roomType = scanner.nextLine();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter base price: ");
        BigDecimal basePrice =
                new BigDecimal(scanner.nextLine());

        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setCapacity(capacity);
        room.setBasePrice(basePrice);
        room.setStatus(status);

        boolean created = roomController.createRoom(room);

        if (created) {
            System.out.println(
                    "Room created successfully. ID: "
                            + room.getRoomId()
            );
        } else {
            System.out.println("Room creation failed.");
        }
    }

    private void findRoom() {
        System.out.println();
        System.out.println("----- FIND ROOM -----");

        System.out.print("Enter Room ID: ");
        long roomId = scanner.nextLong();
        scanner.nextLine();

        Room room = roomController.getRoomById(roomId);

        if (room != null) {
            printRoom(room);
        } else {
            System.out.println("Room not found.");
        }
    }

    private void viewAllRooms() {
        System.out.println();
        System.out.println("----- ALL ROOMS -----");

        List<Room> rooms = roomController.getAllRooms();

        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }

        for (Room room : rooms) {
            printRoom(room);
            System.out.println("------------------------------------");
        }
    }

    private void updateRoom() {
        System.out.println();
        System.out.println("----- UPDATE ROOM -----");

        System.out.print("Enter Room ID: ");
        long roomId = scanner.nextLong();
        scanner.nextLine();

        Room room = roomController.getRoomById(roomId);

        if (room == null) {
            System.out.println("Room not found.");
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

        room.setHotel(hotel);

        System.out.print("Enter room number: ");
        room.setRoomNumber(scanner.nextLine());

        System.out.print("Enter room type: ");
        room.setRoomType(scanner.nextLine());

        System.out.print("Enter capacity: ");
        room.setCapacity(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter base price: ");
        room.setBasePrice(
                new BigDecimal(scanner.nextLine())
        );

        System.out.print("Enter status: ");
        room.setStatus(scanner.nextLine());

        boolean updated = roomController.updateRoom(room);

        System.out.println(
                updated
                        ? "Room updated successfully."
                        : "Room update failed."
        );
    }

    private void deleteRoom() {
        System.out.println();
        System.out.println("----- DELETE ROOM -----");

        System.out.print("Enter Room ID: ");
        long roomId = scanner.nextLong();
        scanner.nextLine();

        Room room = roomController.getRoomById(roomId);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this room? (yes/no): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean deleted = roomController.deleteRoom(roomId);

            System.out.println(
                    deleted
                            ? "Room deleted successfully."
                            : "Room deletion failed."
            );
        } else {
            System.out.println("Room deletion cancelled.");
        }
    }

    private void printRoom(Room room) {
        System.out.println("Room ID: " + room.getRoomId());

        if (room.getHotel() != null) {
            System.out.println(
                    "Hotel ID: " + room.getHotel().getHotelId()
            );

            System.out.println(
                    "Hotel Name: " + room.getHotel().getName()
            );
        } else {
            System.out.println("Hotel: None");
        }

        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Room Type: " + room.getRoomType());
        System.out.println("Capacity: " + room.getCapacity());
        System.out.println("Base Price: " + room.getBasePrice());
        System.out.println("Status: " + room.getStatus());
    }
}