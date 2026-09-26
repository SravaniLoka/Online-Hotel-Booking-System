package com.booking.service;

import com.booking.dao.PaymentDAO;
import com.booking.dao.PaymentDAOImpl;
import com.booking.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    @Override
    public boolean createPayment(Payment payment) {

        logger.info("createPayment() started");

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

        Payment payment = paymentDAO.findById(paymentId);

        if (payment != null) {
            logger.info(
                    "getPaymentById() completed successfully. paymentId={}",
                    paymentId
            );
        } else {
            logger.info(
                    "getPaymentById() completed. Payment not found. paymentId={}",
                    paymentId
            );
        }

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

        logger.info(
                "updatePayment() started. paymentId={}",
                payment.getPaymentId()
        );

        boolean updated = paymentDAO.update(payment);

        if (updated) {
            logger.info(
                    "updatePayment() completed successfully. paymentId={}",
                    payment.getPaymentId()
            );
        } else {
            logger.info(
                    "updatePayment() failed. paymentId={}",
                    payment.getPaymentId()
            );
        }

        return updated;
    }

    @Override
    public boolean deletePayment(long paymentId) {

        logger.info(
                "deletePayment() started. paymentId={}",
                paymentId
        );

        boolean deleted = paymentDAO.delete(paymentId);

        if (deleted) {
            logger.info(
                    "deletePayment() completed successfully. paymentId={}",
                    paymentId
            );
        } else {
            logger.info(
                    "deletePayment() failed. paymentId={}",
                    paymentId
            );
        }

        return deleted;
    }
}