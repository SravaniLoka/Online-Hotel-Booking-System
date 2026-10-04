package com.booking.test;

import com.booking.dao.HotelDAO;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Hotel;
import com.booking.service.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelDAO hotelDAO;

    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelServiceImpl(hotelDAO);
    }

    // =========================
    // CREATE HOTEL
    // =========================

    @Test
    void createHotel_success() {

        Hotel hotel = new Hotel();

        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        when(hotelDAO.create(hotel)).thenReturn(true);

        boolean result = hotelService.createHotel(hotel);

        assertTrue(result);

        verify(hotelDAO, times(1)).create(hotel);
    }

    @Test
    void createHotel_failure() {

        Hotel hotel = new Hotel();

        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        when(hotelDAO.create(hotel)).thenReturn(false);

        boolean result = hotelService.createHotel(hotel);

        assertFalse(result);

        verify(hotelDAO, times(1)).create(hotel);
    }

    // =========================
    // GET HOTEL BY ID
    // =========================

    @Test
    void getHotelById_hotelFound() {

        long hotelId = 1L;
        Hotel hotel = new Hotel();

        when(hotelDAO.findById(hotelId)).thenReturn(hotel);

        Hotel result = hotelService.getHotelById(hotelId);

        assertNotNull(result);
        assertEquals(hotel, result);

        verify(hotelDAO, times(1)).findById(hotelId);
    }

    @Test
    void getHotelById_hotelNotFound() {

        long hotelId = 999L;

        when(hotelDAO.findById(hotelId)).thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelService.getHotelById(hotelId)
                );

        assertEquals(
                "Hotel not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelDAO, times(1)).findById(hotelId);
    }

    // =========================
    // GET ALL HOTELS
    // =========================

    @Test
    void getAllHotels_success() {

        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();

        List<Hotel> hotels =
                Arrays.asList(hotel1, hotel2);

        when(hotelDAO.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(hotels, result);

        verify(hotelDAO, times(1)).findAll();
    }

    @Test
    void getAllHotels_emptyList() {

        when(hotelDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Hotel> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelDAO, times(1)).findAll();
    }

    // =========================
    // UPDATE HOTEL
    // =========================

    @Test
    void updateHotel_success() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(1L);
        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        when(hotelDAO.update(hotel)).thenReturn(true);

        boolean result = hotelService.updateHotel(hotel);

        assertTrue(result);

        verify(hotelDAO, times(1)).update(hotel);
    }

    @Test
    void updateHotel_failure() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(999L);
        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        when(hotelDAO.update(hotel)).thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelService.updateHotel(hotel)
                );

        assertEquals(
                "Hotel not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelDAO, times(1)).update(hotel);
    }

    // =========================
    // DELETE HOTEL
    // =========================

    @Test
    void deleteHotel_success() {

        long hotelId = 1L;

        when(hotelDAO.delete(hotelId)).thenReturn(true);

        boolean result = hotelService.deleteHotel(hotelId);

        assertTrue(result);

        verify(hotelDAO, times(1)).delete(hotelId);
    }

    @Test
    void deleteHotel_failure() {

        long hotelId = 999L;

        when(hotelDAO.delete(hotelId)).thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelService.deleteHotel(hotelId)
                );

        assertEquals(
                "Hotel not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelDAO, times(1)).delete(hotelId);
    }

    // =========================
    // VALIDATION EXCEPTIONS
    // =========================

    @Test
    void createHotel_nullHotel_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.createHotel(null)
                );

        assertEquals(
                "Hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void createHotel_emptyName_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setName("");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.createHotel(hotel)
                );

        assertEquals(
                "Hotel name cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void createHotel_emptyAddress_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setName("Taj Hotel");
        hotel.setAddress("");
        hotel.setStatus("ACTIVE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.createHotel(hotel)
                );

        assertEquals(
                "Hotel address cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void createHotel_emptyStatus_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.createHotel(hotel)
                );

        assertEquals(
                "Hotel status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void getHotelById_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.getHotelById(0)
                );

        assertEquals(
                "Invalid hotel ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void updateHotel_nullHotel_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.updateHotel(null)
                );

        assertEquals(
                "Hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void updateHotel_invalidId_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(0L);
        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.updateHotel(hotel)
                );

        assertEquals(
                "Invalid hotel ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void updateHotel_emptyName_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(1L);
        hotel.setName("");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("ACTIVE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.updateHotel(hotel)
                );

        assertEquals(
                "Hotel name cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void updateHotel_emptyAddress_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(1L);
        hotel.setName("Taj Hotel");
        hotel.setAddress("");
        hotel.setStatus("ACTIVE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.updateHotel(hotel)
                );

        assertEquals(
                "Hotel address cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void updateHotel_emptyStatus_throwsValidationException() {

        Hotel hotel = new Hotel();

        hotel.setHotelId(1L);
        hotel.setName("Taj Hotel");
        hotel.setAddress("Hyderabad");
        hotel.setStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.updateHotel(hotel)
                );

        assertEquals(
                "Hotel status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }

    @Test
    void deleteHotel_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelService.deleteHotel(0)
                );

        assertEquals(
                "Invalid hotel ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelDAO);
    }
}