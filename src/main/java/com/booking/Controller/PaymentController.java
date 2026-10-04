package com.booking.Controller;

import com.booking.model.Payment;
import com.booking.service.PaymentService;
import com.booking.service.PaymentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentController {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController() {
        this.paymentService = new PaymentServiceImpl();
    }

    public boolean createPayment(Payment payment) {

        logger.info("createPayment() requested");

        boolean created = paymentService.createPayment(payment);

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

    public Payment getPaymentById(long paymentId) {

        logger.info(
                "getPaymentById() requested. paymentId={}",
                paymentId
        );

        Payment payment = paymentService.getPaymentById(paymentId);

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

    public List<Payment> getAllPayments() {

        logger.info("getAllPayments() requested");

        List<Payment> payments = paymentService.getAllPayments();

        logger.info(
                "getAllPayments() completed successfully. paymentsFound={}",
                payments.size()
        );

        return payments;
    }

    public boolean updatePayment(Payment payment) {

        logger.info(
                "updatePayment() requested. paymentId={}",
                payment.getPaymentId()
        );

        boolean updated = paymentService.updatePayment(payment);

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

    public boolean deletePayment(long paymentId) {

        logger.info(
                "deletePayment() requested. paymentId={}",
                paymentId
        );

        boolean deleted = paymentService.deletePayment(paymentId);

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