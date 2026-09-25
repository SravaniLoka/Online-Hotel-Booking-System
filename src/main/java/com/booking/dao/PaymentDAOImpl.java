package com.booking.dao;

import com.booking.model.Booking;
import com.booking.model.Payment;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentDAOImpl.class);

    // SQL queries
    private static final String PAYMENT_CREATE_SQL = """
            INSERT INTO payment
            (booking_id, amount, payment_status, transaction_ref, paid_at)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String PAYMENT_FIND_BY_ID_SQL = """
            SELECT payment_id, booking_id, amount,
                   payment_status, transaction_ref, paid_at
            FROM payment
            WHERE payment_id = ?
            """;

    private static final String PAYMENT_FIND_ALL_SQL = """
            SELECT payment_id, booking_id, amount,
                   payment_status, transaction_ref, paid_at
            FROM payment
            ORDER BY payment_id
            """;

    private static final String PAYMENT_UPDATE_SQL = """
            UPDATE payment
            SET booking_id = ?,
                amount = ?,
                payment_status = ?,
                transaction_ref = ?,
                paid_at = ?
            WHERE payment_id = ?
            """;

    private static final String PAYMENT_DELETE_SQL = """
            DELETE FROM payment
            WHERE payment_id = ?
            """;

    @Override
    public boolean create(Payment payment) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     PAYMENT_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    payment.getBooking().getBookingId()
            );
            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );
            statement.setString(
                    3,
                    payment.getPaymentStatus()
            );
            statement.setString(
                    4,
                    payment.getTransactionRef()
            );
            statement.setTimestamp(
                    5,
                    payment.getPaidAt()
            );

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        payment.setPaymentId(keys.getLong(1));
                    }
                }

                logger.info(
                        "create() completed successfully. paymentId={}",
                        payment.getPaymentId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating payment",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public Payment findById(long paymentId) {

        logger.info(
                "findById() started. paymentId={}",
                paymentId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(PAYMENT_FIND_BY_ID_SQL)) {

            statement.setLong(1, paymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Payment payment =
                            mapResultSetToPayment(resultSet);

                    logger.info(
                            "findById() completed successfully. paymentId={}",
                            paymentId
                    );

                    return payment;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding payment. paymentId={}",
                    paymentId,
                    e
            );
        }

        logger.info(
                "findById() completed. Payment not found. paymentId={}",
                paymentId
        );

        return null;
    }

    @Override
    public List<Payment> findAll() {

        logger.info("findAll() started");

        List<Payment> payments = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(PAYMENT_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                payments.add(
                        mapResultSetToPayment(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. paymentsFound={}",
                    payments.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all payments",
                    e
            );
        }

        return payments;
    }

    @Override
    public boolean update(Payment payment) {

        logger.info(
                "update() started. paymentId={}",
                payment.getPaymentId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(PAYMENT_UPDATE_SQL)) {

            statement.setLong(
                    1,
                    payment.getBooking().getBookingId()
            );
            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );
            statement.setString(
                    3,
                    payment.getPaymentStatus()
            );
            statement.setString(
                    4,
                    payment.getTransactionRef()
            );
            statement.setTimestamp(
                    5,
                    payment.getPaidAt()
            );
            statement.setLong(
                    6,
                    payment.getPaymentId()
            );

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. paymentId={}",
                        payment.getPaymentId()
                );

            } else {

                logger.info(
                        "update() completed. No payment updated. paymentId={}",
                        payment.getPaymentId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating payment. paymentId={}",
                    payment.getPaymentId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long paymentId) {

        logger.info(
                "delete() started. paymentId={}",
                paymentId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(PAYMENT_DELETE_SQL)) {

            statement.setLong(1, paymentId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. paymentId={}",
                        paymentId
                );

            } else {

                logger.info(
                        "delete() completed. No payment deleted. paymentId={}",
                        paymentId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting payment. paymentId={}",
                    paymentId,
                    e
            );
        }

        return false;
    }

    private Payment mapResultSetToPayment(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToPayment() started");

        Booking booking = new Booking();

        booking.setBookingId(
                resultSet.getLong("booking_id")
        );

        Payment payment = new Payment(
                resultSet.getLong("payment_id"),
                booking,
                resultSet.getBigDecimal("amount"),
                resultSet.getString("payment_status"),
                resultSet.getString("transaction_ref"),
                resultSet.getTimestamp("paid_at")
        );

        logger.info(
                "mapResultSetToPayment() completed successfully. paymentId={}",
                payment.getPaymentId()
        );

        return payment;
    }
}