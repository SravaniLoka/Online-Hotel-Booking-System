package com.booking.dao;

import com.booking.model.Payment;

import java.sql.Connection;
import java.util.List;

public interface PaymentDAO {

    boolean create(Payment payment);

    boolean create(Payment payment, Connection connection);

    Payment findById(long paymentId);

    List<Payment> findAll();

    boolean update(Payment payment);

    boolean update(Payment payment, Connection connection);

    boolean delete(long paymentId);
}