package com.booking.dao;

import com.booking.model.Payment;

import java.util.List;

public interface PaymentDAO {

    boolean create(Payment payment);

    Payment findById(long paymentId);

    List<Payment> findAll();

    boolean update(Payment payment);

    boolean delete(long paymentId);
}