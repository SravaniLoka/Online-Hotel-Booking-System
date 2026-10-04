
        package com.booking.menu;

import com.booking.ConsoleScanner;
import com.booking.Controller.BookingController;
import com.booking.Controller.PaymentController;
import com.booking.model.Booking;
import com.booking.model.Payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class PaymentMenu {

    private final PaymentController paymentController;
    private final BookingController bookingController;
    private final ConsoleScanner scanner;

    public PaymentMenu(
            PaymentController paymentController,
            BookingController bookingController,
            ConsoleScanner scanner) {

        this.paymentController = paymentController;
        this.bookingController = bookingController;
        this.scanner = scanner;
    }

    public void start() {

        boolean paymentMenuRunning = true;

        while (paymentMenuRunning) {

            System.out.println();
            System.out.println("----- PAYMENT MANAGEMENT -----");
            System.out.println("1. Create Payment");
            System.out.println("2. Find Payment");
            System.out.println("3. View All Payments");
            System.out.println("4. Update Payment");
            System.out.println("5. Delete Payment");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createPayment();
                    break;

                case 2:
                    findPayment();
                    break;

                case 3:
                    viewAllPayments();
                    break;

                case 4:
                    updatePayment();
                    break;

                case 5:
                    deletePayment();
                    break;

                case 6:
                    paymentMenuRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void createPayment() {

        System.out.println();
        System.out.println("----- CREATE PAYMENT -----");

        System.out.print("Enter Booking ID: ");
        long bookingId = scanner.nextLong();
        scanner.nextLine();

        Booking booking =
                bookingController.getBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        System.out.print("Enter amount: ");
        BigDecimal amount =
                new BigDecimal(scanner.nextLine());

        System.out.print("Enter payment status: ");
        String paymentStatus = scanner.nextLine();

        System.out.print("Enter transaction reference: ");
        String transactionRef = scanner.nextLine();

        System.out.print(
                "Enter paid-at timestamp (yyyy-[m]m-[d]d hh:mm:ss): "
        );
        Timestamp paidAt =
                Timestamp.valueOf(scanner.nextLine());

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentStatus(paymentStatus);
        payment.setTransactionRef(transactionRef);
        payment.setPaidAt(paidAt);

        boolean created =
                paymentController.createPayment(payment);

        if (created) {
            System.out.println(
                    "Payment created successfully. ID: "
                            + payment.getPaymentId()
            );
        } else {
            System.out.println("Payment creation failed.");
        }
    }

    private void findPayment() {

        System.out.println();
        System.out.println("----- FIND PAYMENT -----");

        System.out.print("Enter Payment ID: ");
        long paymentId = scanner.nextLong();

        Payment payment =
                paymentController.getPaymentById(paymentId);

        if (payment != null) {
            printPayment(payment);
        } else {
            System.out.println("Payment not found.");
        }
    }

    private void viewAllPayments() {

        System.out.println();
        System.out.println("----- ALL PAYMENTS -----");

        List<Payment> payments =
                paymentController.getAllPayments();

        if (payments == null || payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        for (Payment payment : payments) {
            printPayment(payment);
            System.out.println("------------------------------------");
        }
    }

    private void updatePayment() {

        System.out.println();
        System.out.println("----- UPDATE PAYMENT -----");

        System.out.print("Enter Payment ID: ");
        long paymentId = scanner.nextLong();
        scanner.nextLine();

        Payment payment =
                paymentController.getPaymentById(paymentId);

        if (payment == null) {
            System.out.println("Payment not found.");
            return;
        }

        System.out.print("Enter Booking ID: ");
        long bookingId = scanner.nextLong();
        scanner.nextLine();

        Booking booking =
                bookingController.getBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        payment.setBooking(booking);

        System.out.print("Enter amount: ");
        payment.setAmount(
                new BigDecimal(scanner.nextLine())
        );

        System.out.print("Enter payment status: ");
        payment.setPaymentStatus(scanner.nextLine());

        System.out.print("Enter transaction reference: ");
        payment.setTransactionRef(scanner.nextLine());

        System.out.print(
                "Enter paid-at timestamp (yyyy-[m]m-[d]d hh:mm:ss): "
        );
        payment.setPaidAt(
                Timestamp.valueOf(scanner.nextLine())
        );

        boolean updated =
                paymentController.updatePayment(payment);

        System.out.println(
                updated
                        ? "Payment updated successfully."
                        : "Payment update failed."
        );
    }

    private void deletePayment() {

        System.out.println();
        System.out.println("----- DELETE PAYMENT -----");

        System.out.print("Enter Payment ID: ");
        long paymentId = scanner.nextLong();

        Payment payment =
                paymentController.getPaymentById(paymentId);

        if (payment == null) {
            System.out.println("Payment not found.");
            return;
        }

        System.out.print(
                "Are you sure you want to delete this payment? (yes/no): "
        );

        scanner.nextLine();
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {

            boolean deleted =
                    paymentController.deletePayment(paymentId);

            System.out.println(
                    deleted
                            ? "Payment deleted successfully."
                            : "Payment deletion failed."
            );

        } else {
            System.out.println("Payment deletion cancelled.");
        }
    }

    private void printPayment(Payment payment) {

        System.out.println(
                "Payment ID: " + payment.getPaymentId()
        );

        if (payment.getBooking() != null) {
            System.out.println(
                    "Booking ID: "
                            + payment.getBooking().getBookingId()
            );
        } else {
            System.out.println("Booking: None");
        }

        System.out.println("Amount: " + payment.getAmount());

        System.out.println(
                "Payment Status: " + payment.getPaymentStatus()
        );

        System.out.println(
                "Transaction Reference: "
                        + payment.getTransactionRef()
        );

        System.out.println("Paid At: " + payment.getPaidAt());
    }
}