package com.booking.service;

import com.booking.model.Payment;

import java.util.List;

public interface PaymentService {

    boolean createPayment(Payment payment);

    Payment getPaymentById(long paymentId);

    List<Payment> getAllPayments();

    boolean updatePayment(Payment payment);

    boolean deletePayment(long paymentId);
}