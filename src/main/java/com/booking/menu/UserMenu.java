
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.UserController;
import com.booking.model.User;

import java.util.List;

public class UserMenu {

    private final UserController userController;
    private final ConsoleScanner scanner;

    public UserMenu(
            UserController userController,
            ConsoleScanner scanner
    ) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public void start() {
        boolean userMenuRunning = true;

        while (userMenuRunning) {
            System.out.println();
            System.out.println("----- USER MANAGEMENT -----");
            System.out.println("1. Create User");
            System.out.println("2. Find User");
            System.out.println("3. View All Users");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");

            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {

                case 1:
                    createUser();
                    break;

                case 2:
                    findUser();
                    break;

                case 3:
                    viewAllUsers();
                    break;

                case 4:
                    updateUser();
                    break;

                case 5:
                    deleteUser();
                    break;

                case 6:
                    userMenuRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createUser() {
        System.out.println();
        System.out.println("----- CREATE USER -----");

        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextPassword();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter role: ");
        String role = scanner.nextLine();

        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setStatus(status);

        boolean created = userController.createUser(user);

        if (created) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("          USER CREATED!");
            System.out.println("====================================");
            System.out.println("User ID: " + user.getUserId());
        } else {
            System.out.println();
            System.out.println("User creation failed.");
        }
    }

    private void findUser() {
        System.out.println();
        System.out.println("----- FIND USER -----");

        System.out.print("Enter User ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();

        User foundUser = userController.getUserById(userId);

        if (foundUser != null) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("             USER FOUND");
            System.out.println("====================================");
            printUser(foundUser);
        } else {
            System.out.println();
            System.out.println("User not found.");
        }
    }

    private void viewAllUsers() {
        System.out.println();
        System.out.println("----- ALL USERS -----");

        List<User> users = userController.getAllUsers();

        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User currentUser : users) {
            printUser(currentUser);
            System.out.println("------------------------------------");
        }
    }

    private void updateUser() {
        System.out.println();
        System.out.println("----- UPDATE USER -----");

        System.out.print("Enter User ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();

        User userToUpdate = userController.getUserById(userId);

        if (userToUpdate == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter full name: ");
        userToUpdate.setFullName(scanner.nextLine());

        System.out.print("Enter email: ");
        userToUpdate.setEmail(scanner.nextLine());

        System.out.print("Enter password: ");
        userToUpdate.setPasswordHash(scanner.nextPassword());

        System.out.print("Enter phone: ");
        userToUpdate.setPhone(scanner.nextLine());

        System.out.print("Enter role: ");
        userToUpdate.setRole(scanner.nextLine());

        System.out.print("Enter status: ");
        userToUpdate.setStatus(scanner.nextLine());

        boolean updated = userController.updateUser(userToUpdate);

        if (updated) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("          USER UPDATED!");
            System.out.println("====================================");
        } else {
            System.out.println();
            System.out.println("User update failed.");
        }
    }

    private void deleteUser() {
        System.out.println();
        System.out.println("----- DELETE USER -----");

        System.out.print("Enter User ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();

        User userToDelete = userController.getUserById(userId);

        if (userToDelete == null) {
            System.out.println();
            System.out.println("User not found.");
            return;
        }

        System.out.println();
        System.out.println("User found:");
        printUser(userToDelete);

        System.out.print(
                "Are you sure you want to delete this user? (yes/no): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean deleted = userController.deleteUser(userId);

            if (deleted) {
                System.out.println();
                System.out.println("====================================");
                System.out.println("          USER DELETED!");
                System.out.println("====================================");
            } else {
                System.out.println();
                System.out.println("User deletion failed.");
            }
        } else {
            System.out.println();
            System.out.println("User deletion cancelled.");
        }
    }

    private void printUser(User user) {
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Role: " + user.getRole());
        System.out.println("Status: " + user.getStatus());
    }
}
