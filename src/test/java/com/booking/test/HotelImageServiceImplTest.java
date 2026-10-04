package com.booking.test;

import com.booking.dao.HotelImageDAO;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Hotel;
import com.booking.model.HotelImage;
import com.booking.service.HotelImageServiceImpl;
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
class HotelImageServiceImplTest {

    @Mock
    private HotelImageDAO hotelImageDAO;

    private HotelImageServiceImpl hotelImageService;

    @BeforeEach
    void setUp() {
        hotelImageService = new HotelImageServiceImpl(hotelImageDAO);
    }

    // =========================================================
    // Helper method
    // =========================================================

    private HotelImage createValidHotelImage() {

        HotelImage hotelImage = new HotelImage();

        Hotel hotel = new Hotel();

        hotelImage.setHotel(hotel);
        hotelImage.setImageUrl("https://example.com/hotel.jpg");
        hotelImage.setCaption("Hotel exterior");

        return hotelImage;
    }

    // =========================================================
    // CREATE HOTEL IMAGE
    // =========================================================

    @Test
    void createHotelImage_success() {

        HotelImage hotelImage = createValidHotelImage();

        when(hotelImageDAO.create(hotelImage)).thenReturn(true);

        boolean result =
                hotelImageService.createHotelImage(hotelImage);

        assertTrue(result);

        verify(hotelImageDAO, times(1))
                .create(hotelImage);
    }

    @Test
    void createHotelImage_failure() {

        HotelImage hotelImage = createValidHotelImage();

        when(hotelImageDAO.create(hotelImage)).thenReturn(false);

        boolean result =
                hotelImageService.createHotelImage(hotelImage);

        assertFalse(result);

        verify(hotelImageDAO, times(1))
                .create(hotelImage);
    }

    // =========================================================
    // GET HOTEL IMAGE BY ID
    // =========================================================

    @Test
    void getHotelImageById_imageFound() {

        long imageId = 1L;

        HotelImage hotelImage = createValidHotelImage();

        when(hotelImageDAO.findById(imageId))
                .thenReturn(hotelImage);

        HotelImage result =
                hotelImageService.getHotelImageById(imageId);

        assertNotNull(result);
        assertEquals(hotelImage, result);

        verify(hotelImageDAO, times(1))
                .findById(imageId);
    }

    @Test
    void getHotelImageById_imageNotFound() {

        long imageId = 999L;

        when(hotelImageDAO.findById(imageId))
                .thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelImageService
                                .getHotelImageById(imageId)
                );

        assertEquals(
                "Hotel image not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelImageDAO, times(1))
                .findById(imageId);
    }

    // =========================================================
    // GET ALL HOTEL IMAGES
    // =========================================================

    @Test
    void getAllHotelImages_success() {

        HotelImage image1 = createValidHotelImage();
        HotelImage image2 = createValidHotelImage();

        List<HotelImage> images =
                Arrays.asList(image1, image2);

        when(hotelImageDAO.findAll())
                .thenReturn(images);

        List<HotelImage> result =
                hotelImageService.getAllHotelImages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(images, result);

        verify(hotelImageDAO, times(1))
                .findAll();
    }

    @Test
    void getAllHotelImages_emptyList() {

        when(hotelImageDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<HotelImage> result =
                hotelImageService.getAllHotelImages();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelImageDAO, times(1))
                .findAll();
    }

    // =========================================================
    // UPDATE HOTEL IMAGE
    // =========================================================

    @Test
    void updateHotelImage_success() {

        HotelImage hotelImage = createValidHotelImage();

        hotelImage.setImageId(1L);

        when(hotelImageDAO.update(hotelImage))
                .thenReturn(true);

        boolean result =
                hotelImageService.updateHotelImage(hotelImage);

        assertTrue(result);

        verify(hotelImageDAO, times(1))
                .update(hotelImage);
    }

    @Test
    void updateHotelImage_notFound() {

        HotelImage hotelImage = createValidHotelImage();

        hotelImage.setImageId(999L);

        when(hotelImageDAO.update(hotelImage))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelImageService
                                .updateHotelImage(hotelImage)
                );

        assertEquals(
                "Hotel image not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelImageDAO, times(1))
                .update(hotelImage);
    }

    // =========================================================
    // DELETE HOTEL IMAGE
    // =========================================================

    @Test
    void deleteHotelImage_success() {

        long imageId = 1L;

        when(hotelImageDAO.delete(imageId))
                .thenReturn(true);

        boolean result =
                hotelImageService.deleteHotelImage(imageId);

        assertTrue(result);

        verify(hotelImageDAO, times(1))
                .delete(imageId);
    }

    @Test
    void deleteHotelImage_notFound() {

        long imageId = 999L;

        when(hotelImageDAO.delete(imageId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelImageService
                                .deleteHotelImage(imageId)
                );

        assertEquals(
                "Hotel image not found with ID: 999",
                exception.getMessage()
        );

        verify(hotelImageDAO, times(1))
                .delete(imageId);
    }

    // =========================================================
    // CREATE VALIDATION EXCEPTIONS
    // =========================================================

    @Test
    void createHotelImage_nullImage() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .createHotelImage(null)
                );

        assertEquals(
                "Hotel image cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    @Test
    void createHotelImage_nullHotel() {

        HotelImage hotelImage = new HotelImage();

        hotelImage.setHotel(null);
        hotelImage.setImageUrl(
                "https://example.com/hotel.jpg"
        );

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .createHotelImage(hotelImage)
                );

        assertEquals(
                "Hotel image hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    @Test
    void createHotelImage_emptyImageUrl() {

        HotelImage hotelImage =
                createValidHotelImage();

        hotelImage.setImageUrl("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .createHotelImage(hotelImage)
                );

        assertEquals(
                "Image URL cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    // =========================================================
    // GET VALIDATION EXCEPTION
    // =========================================================

    @Test
    void getHotelImageById_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .getHotelImageById(0)
                );

        assertEquals(
                "Invalid image ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    // =========================================================
    // UPDATE VALIDATION EXCEPTIONS
    // =========================================================

    @Test
    void updateHotelImage_nullImage() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .updateHotelImage(null)
                );

        assertEquals(
                "Hotel image cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    @Test
    void updateHotelImage_invalidId() {

        HotelImage hotelImage =
                createValidHotelImage();

        hotelImage.setImageId(0L);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .updateHotelImage(hotelImage)
                );

        assertEquals(
                "Invalid image ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    @Test
    void updateHotelImage_nullHotel() {

        HotelImage hotelImage =
                createValidHotelImage();

        hotelImage.setImageId(1L);
        hotelImage.setHotel(null);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .updateHotelImage(hotelImage)
                );

        assertEquals(
                "Hotel image hotel cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    @Test
    void updateHotelImage_emptyImageUrl() {

        HotelImage hotelImage =
                createValidHotelImage();

        hotelImage.setImageId(1L);
        hotelImage.setImageUrl("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .updateHotelImage(hotelImage)
                );

        assertEquals(
                "Image URL cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }

    // =========================================================
    // DELETE VALIDATION EXCEPTION
    // =========================================================

    @Test
    void deleteHotelImage_invalidId() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> hotelImageService
                                .deleteHotelImage(0)
                );

        assertEquals(
                "Invalid image ID",
                exception.getMessage()
        );

        verifyNoInteractions(hotelImageDAO);
    }
}