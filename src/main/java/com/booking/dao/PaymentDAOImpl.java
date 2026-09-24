package com.booking.dao;

import com.booking.model.Booking;
import com.booking.model.Payment;
import com.booking.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean create(Payment payment) {

        String sql = """
                INSERT INTO payment
                (booking_id, amount, payment_status, transaction_ref, paid_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, payment.getBooking().getBookingId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentStatus());
            statement.setString(4, payment.getTransactionRef());
            statement.setTimestamp(5, payment.getPaidAt());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        payment.setPaymentId(keys.getLong(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Payment findById(long paymentId) {

        String sql = """
                SELECT payment_id, booking_id, amount,
                       payment_status, transaction_ref, paid_at
                FROM payment
                WHERE payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, paymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToPayment(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Payment> findAll() {

        List<Payment> payments = new ArrayList<>();

        String sql = """
                SELECT payment_id, booking_id, amount,
                       payment_status, transaction_ref, paid_at
                FROM payment
                ORDER BY payment_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                payments.add(
                        mapResultSetToPayment(resultSet)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    @Override
    public boolean update(Payment payment) {

        String sql = """
                UPDATE payment
                SET booking_id = ?,
                    amount = ?,
                    payment_status = ?,
                    transaction_ref = ?,
                    paid_at = ?
                WHERE payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, payment.getBooking().getBookingId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentStatus());
            statement.setString(4, payment.getTransactionRef());
            statement.setTimestamp(5, payment.getPaidAt());
            statement.setLong(6, payment.getPaymentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long paymentId) {

        String sql = """
                DELETE FROM payment
                WHERE payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, paymentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Payment mapResultSetToPayment(ResultSet resultSet)
            throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(
                resultSet.getLong("booking_id")
        );

        return new Payment(
                resultSet.getLong("payment_id"),
                booking,
                resultSet.getBigDecimal("amount"),
                resultSet.getString("payment_status"),
                resultSet.getString("transaction_ref"),
                resultSet.getTimestamp("paid_at")
        );
    }
}