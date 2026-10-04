
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.UserController;
import com.booking.model.User;

import java.util.List;

public class AccountMenu {

    private final UserController userController;
    private final ConsoleScanner scanner;

    public AccountMenu(
            UserController userController,
            ConsoleScanner scanner
    ) {
        this.userController = userController;
        this.scanner = scanner;
    }

    // Authentication menu
    // Returns the logged-in user, or null when Exit is selected.
    public User start() {

        while (true) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("       BB&J HOTEL BOOKING");
            System.out.println("====================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Please enter a number."
                );
                continue;
            }

            switch (choice) {

                case 1: {
                    int registrationResult = registerUser();

                    if (registrationResult == 1) {
                        System.out.println();
                        System.out.println(
                                "Registration successful! Please log in."
                        );

                        // Go directly to login after registration.
                        User loggedInUser = loginUser();

                        if (loggedInUser != null) {
                            return loggedInUser;
                        }
                    } else if (registrationResult == 2) {
                        System.out.println(
                                "Please choose Login to access your account."
                        );
                    }

                    break;
                }

                case 2: {
                    User loggedInUser = loginUser();

                    if (loggedInUser != null) {
                        return loggedInUser;
                    }

                    break;
                }

                case 3:
                    System.out.println(
                            "You are already logged out."
                    );
                    System.out.println(
                            "Please log in or choose Exit."
                    );
                    break;

                case 4:
                    System.out.println(
                            "Thank you for visiting BB&J!"
                    );
                    return null;

                default:
                    System.out.println(
                            "Invalid choice. Please select 1, 2, 3, or 4."
                    );
            }
        }
    }

    // Registration
    public int registerUser() {

        System.out.println();
        System.out.println("====================================");
        System.out.println("          REGISTRATION");
        System.out.println("====================================");

        System.out.print("Enter your name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        if (fullName.isEmpty() || email.isEmpty()) {
            System.out.println(
                    "Name and email cannot be empty."
            );
            return 0;
        }

        User existingUser = findUserByEmail(email);

        if (existingUser != null) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("        ALREADY REGISTERED");
            System.out.println("====================================");
            System.out.println(
                    "An account with this email already exists."
            );

            return 2;
        }

        System.out.print("Create password: ");
        String password = scanner.nextPassword();

        if (password == null || password.isEmpty()) {
            System.out.println();
            System.out.println("Password cannot be empty.");
            return 0;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(password);
        newUser.setRole("CUSTOMER");
        newUser.setStatus("ACTIVE");

        boolean created = userController.createUser(newUser);

        if (created) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("     REGISTRATION SUCCESSFUL");
            System.out.println("====================================");
            System.out.println(
                    "Your account has been created successfully."
            );

            return 1;
        }

        System.out.println();
        System.out.println(
                "Registration failed. Please try again."
        );

        return 0;
    }

    // Login
    public User loginUser() {

        System.out.println();
        System.out.println("====================================");
        System.out.println("              LOGIN");
        System.out.println("====================================");

        System.out.print("Enter your name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextPassword();

        User existingUser = findUserByEmail(email);

        if (existingUser == null) {
            System.out.println();
            System.out.println("====================================");
            System.out.println("       ACCOUNT NOT REGISTERED");
            System.out.println("====================================");
            System.out.println(
                    "Please register before logging in."
            );

            return null;
        }

        boolean validName =
                existingUser.getFullName() != null
                        && existingUser.getFullName()
                        .equalsIgnoreCase(fullName);

        boolean validEmail =
                existingUser.getEmail() != null
                        && existingUser.getEmail()
                        .equalsIgnoreCase(email);

        boolean validPassword =
                existingUser.getPasswordHash() != null
                        && existingUser.getPasswordHash()
                        .equals(password);

        boolean activeAccount =
                existingUser.getStatus() != null
                        && existingUser.getStatus()
                        .equalsIgnoreCase("ACTIVE");

        if (validName && validEmail
                && validPassword && activeAccount) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("          LOGIN SUCCESSFUL");
            System.out.println("====================================");
            System.out.println(
                    "Welcome, " + existingUser.getFullName() + "!"
            );
            System.out.println(
                    "Role: " + existingUser.getRole()
            );

            return existingUser;
        }

        System.out.println();
        System.out.println(
                "Login failed. Check your name, email, and password."
        );

        if (!activeAccount) {
            System.out.println(
                    "Your account is inactive. Please contact the administrator."
            );
        }

        return null;
    }

    // Find a user using their email
    private User findUserByEmail(String email) {

        List<User> users = userController.getAllUsers();

        if (users == null) {
            return null;
        }

        for (User user : users) {
            if (user != null
                    && user.getEmail() != null
                    && user.getEmail().trim()
                    .equalsIgnoreCase(email.trim())) {

                return user;
            }
        }

        return null;
    }
}
