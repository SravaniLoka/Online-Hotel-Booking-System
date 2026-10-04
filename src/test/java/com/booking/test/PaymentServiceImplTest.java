package com.booking.test;

import com.booking.dao.PaymentDAO;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Booking;
import com.booking.model.Payment;
import com.booking.service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentDAO paymentDAO;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentDAO);
    }

    // =========================================================
    // Helper method
    // =========================================================

    private Payment createValidPayment() {

        Payment payment = new Payment();

        Booking booking = new Booking();

        payment.setBooking(booking);
        payment.setAmount(new BigDecimal("10000.00"));
        payment.setPaymentStatus("PAID");

        return payment;
    }

    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @Test
    void createPayment_success() {

        Payment payment = createValidPayment();

        when(paymentDAO.create(payment)).thenReturn(true);

        boolean result = paymentService.createPayment(payment);

        assertTrue(result);

        verify(paymentDAO, times(1)).create(payment);
    }

    @Test
    void createPayment_failure() {

        Payment payment = createValidPayment();

        when(paymentDAO.create(payment)).thenReturn(false);

        boolean result = paymentService.createPayment(payment);

        assertFalse(result);

        verify(paymentDAO, times(1)).create(payment);
    }

    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Test
    void getPaymentById_paymentFound() {

        long paymentId = 1L;

        Payment payment = createValidPayment();

        when(paymentDAO.findById(paymentId))
                .thenReturn(payment);

        Payment result =
                paymentService.getPaymentById(paymentId);

        assertNotNull(result);
        assertEquals(payment, result);

        verify(paymentDAO, times(1))
                .findById(paymentId);
    }

    @Test
    void getPaymentById_paymentNotFound() {

        long paymentId = 999L;

        when(paymentDAO.findById(paymentId))
                .thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> paymentService.getPaymentById(paymentId)
                );

        assertEquals(
                "Payment not found with ID: 999",
                exception.getMessage()
        );

        verify(paymentDAO, times(1))
                .findById(paymentId);
    }

    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    @Test
    void getAllPayments_success() {

        Payment payment1 = createValidPayment();
        Payment payment2 = createValidPayment();

        List<Payment> payments =
                Arrays.asList(payment1, payment2);

        when(paymentDAO.findAll())
                .thenReturn(payments);

        List<Payment> result =
                paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(payments, result);

        verify(paymentDAO, times(1))
                .findAll();
    }

    @Test
    void getAllPayments_emptyList() {

        when(paymentDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Payment> result =
                paymentService.getAllPayments();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(paymentDAO, times(1))
                .findAll();
    }

    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    @Test
    void updatePayment_success() {

        Payment payment = createValidPayment();

        payment.setPaymentId(1L);

        when(paymentDAO.update(payment))
                .thenReturn(true);

        boolean result =
                paymentService.updatePayment(payment);

        assertTrue(result);

        verify(paymentDAO, times(1))
                .update(payment);
    }

    @Test
    void updatePayment_notFound() {

        Payment payment = createValidPayment();

        payment.setPaymentId(999L);

        when(paymentDAO.update(payment))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Payment not found with ID: 999",
                exception.getMessage()
        );

        verify(paymentDAO, times(1))
                .update(payment);
    }

    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @Test
    void deletePayment_success() {

        long paymentId = 1L;

        when(paymentDAO.delete(paymentId))
                .thenReturn(true);

        boolean result =
                paymentService.deletePayment(paymentId);

        assertTrue(result);

        verify(paymentDAO, times(1))
                .delete(paymentId);
    }

    @Test
    void deletePayment_notFound() {

        long paymentId = 999L;

        when(paymentDAO.delete(paymentId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> paymentService.deletePayment(paymentId)
                );

        assertEquals(
                "Payment not found with ID: 999",
                exception.getMessage()
        );

        verify(paymentDAO, times(1))
                .delete(paymentId);
    }

    // =========================================================
    // VALIDATION EXCEPTIONS - CREATE
    // =========================================================

    @Test
    void createPayment_nullPayment() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.createPayment(null)
                );

        assertEquals(
                "Payment cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void createPayment_nullBooking() {

        Payment payment = new Payment();

        payment.setBooking(null);
        payment.setAmount(new BigDecimal("10000.00"));
        payment.setPaymentStatus("PAID");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.createPayment(payment)
                );

        assertEquals(
                "Payment booking cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void createPayment_negativeAmount() {

        Payment payment = createValidPayment();

        payment.setAmount(new BigDecimal("-100.00"));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.createPayment(payment)
                );

        assertEquals(
                "Payment amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void createPayment_nullAmount() {

        Payment payment = createValidPayment();

        payment.setAmount(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.createPayment(payment)
                );

        assertEquals(
                "Payment amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void createPayment_emptyStatus() {

        Payment payment = createValidPayment();

        payment.setPaymentStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.createPayment(payment)
                );

        assertEquals(
                "Payment status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    // =========================================================
    // VALIDATION EXCEPTIONS - GET
    // =========================================================

    @Test
    void getPaymentById_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.getPaymentById(0)
                );

        assertEquals(
                "Invalid payment ID",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    // =========================================================
    // VALIDATION EXCEPTIONS - UPDATE
    // =========================================================

    @Test
    void updatePayment_nullPayment() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(null)
                );

        assertEquals(
                "Payment cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void updatePayment_invalidId() {

        Payment payment = createValidPayment();

        payment.setPaymentId(0L);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Invalid payment ID",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void updatePayment_nullBooking() {

        Payment payment = createValidPayment();

        payment.setPaymentId(1L);
        payment.setBooking(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Payment booking cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void updatePayment_negativeAmount() {

        Payment payment = createValidPayment();

        payment.setPaymentId(1L);
        payment.setAmount(new BigDecimal("-100.00"));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Payment amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void updatePayment_nullAmount() {

        Payment payment = createValidPayment();

        payment.setPaymentId(1L);
        payment.setAmount(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Payment amount cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    @Test
    void updatePayment_emptyStatus() {

        Payment payment = createValidPayment();

        payment.setPaymentId(1L);
        payment.setPaymentStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.updatePayment(payment)
                );

        assertEquals(
                "Payment status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }

    // =========================================================
    // VALIDATION EXCEPTIONS - DELETE
    // =========================================================

    @Test
    void deletePayment_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> paymentService.deletePayment(0)
                );

        assertEquals(
                "Invalid payment ID",
                exception.getMessage()
        );

        verifyNoInteractions(paymentDAO);
    }
}