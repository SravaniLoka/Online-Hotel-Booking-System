package com.booking.service;

import com.booking.dao.PaymentDAO;
import com.booking.dao.PaymentDAOImpl;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    public PaymentServiceImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public boolean createPayment(Payment payment) {

        logger.info("createPayment() started");

        // Validation
        if (payment == null) {
            throw new ValidationException("Payment cannot be null");
        }

        if (payment.getBooking() == null) {
            throw new ValidationException("Payment booking cannot be null");
        }

        if (payment.getAmount() == null ||
                payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Payment amount cannot be negative"
            );
        }

        if (payment.getPaymentStatus() == null ||
                payment.getPaymentStatus().trim().isEmpty()) {
            throw new ValidationException(
                    "Payment status cannot be empty"
            );
        }

        boolean created = paymentDAO.create(payment);

        if (created) {
            logger.info(
                    "createPayment() completed successfully. paymentId={}",
                    payment.getPaymentId()
            );
        } else {
            logger.info("createPayment() failed");
        }

        return created;
    }

    @Override
    public Payment getPaymentById(long paymentId) {

        logger.info(
                "getPaymentById() started. paymentId={}",
                paymentId
        );

        // Validation
        if (paymentId <= 0) {
            throw new ValidationException(
                    "Invalid payment ID"
            );
        }

        Payment payment = paymentDAO.findById(paymentId);

        if (payment == null) {
            logger.info(
                    "getPaymentById() completed. Payment not found. paymentId={}",
                    paymentId
            );

            throw new ResourceNotFoundException(
                    "Payment not found with ID: " + paymentId
            );
        }

        logger.info(
                "getPaymentById() completed successfully. paymentId={}",
                paymentId
        );

        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {

        logger.info("getAllPayments() started");

        List<Payment> payments = paymentDAO.findAll();

        logger.info(
                "getAllPayments() completed successfully. paymentsFound={}",
                payments.size()
        );

        return payments;
    }

    @Override
    public boolean updatePayment(Payment payment) {

        logger.info("updatePayment() started");

        // Validation
        if (payment == null) {
            throw new ValidationException(
                    "Payment cannot be null"
            );
        }

        logger.info(
                "updatePayment() started. paymentId={}",
                payment.getPaymentId()
        );

        if (payment.getPaymentId() <= 0) {
            throw new ValidationException(
                    "Invalid payment ID"
            );
        }

        if (payment.getBooking() == null) {
            throw new ValidationException(
                    "Payment booking cannot be null"
            );
        }

        if (payment.getAmount() == null ||
                payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Payment amount cannot be negative"
            );
        }

        if (payment.getPaymentStatus() == null ||
                payment.getPaymentStatus().trim().isEmpty()) {
            throw new ValidationException(
                    "Payment status cannot be empty"
            );
        }

        boolean updated = paymentDAO.update(payment);

        if (!updated) {
            logger.info(
                    "updatePayment() failed. paymentId={}",
                    payment.getPaymentId()
            );

            throw new ResourceNotFoundException(
                    "Payment not found with ID: " +
                            payment.getPaymentId()
            );
        }

        logger.info(
                "updatePayment() completed successfully. paymentId={}",
                payment.getPaymentId()
        );

        return true;
    }

    @Override
    public boolean deletePayment(long paymentId) {

        logger.info(
                "deletePayment() started. paymentId={}",
                paymentId
        );

        // Validation
        if (paymentId <= 0) {
            throw new ValidationException(
                    "Invalid payment ID"
            );
        }

        boolean deleted = paymentDAO.delete(paymentId);

        if (!deleted) {
            logger.info(
                    "deletePayment() failed. paymentId={}",
                    paymentId
            );

            throw new ResourceNotFoundException(
                    "Payment not found with ID: " + paymentId
            );
        }

        logger.info(
                "deletePayment() completed successfully. paymentId={}",
                paymentId
        );

        return true;
    }
}