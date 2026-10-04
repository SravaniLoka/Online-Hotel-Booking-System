package com.booking.test;

import com.booking.dao.LocationDAO;
import com.booking.exception.ValidationException;
import com.booking.model.Location;
import com.booking.service.LocationServiceImpl;
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
class LocationServiceImplTest {

    @Mock
    private LocationDAO locationDAO;

    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl(locationDAO);
    }

    @Test
    void createLocation_success() {

        Location location = new Location();

        location.setName("Hyderabad");
        location.setType("CITY");

        when(locationDAO.create(location)).thenReturn(true);

        boolean result = locationService.createLocation(location);

        assertTrue(result);

        verify(locationDAO, times(1)).create(location);
    }

    @Test
    void createLocation_failure() {

        Location location = new Location();

        location.setName("Hyderabad");
        location.setType("CITY");

        when(locationDAO.create(location)).thenReturn(false);

        boolean result = locationService.createLocation(location);

        assertFalse(result);

        verify(locationDAO, times(1)).create(location);
    }

    @Test
    void getLocationById_locationFound() {

        long locationId = 1L;
        Location location = new Location();

        when(locationDAO.findById(locationId)).thenReturn(location);

        Location result = locationService.getLocationById(locationId);

        assertNotNull(result);
        assertEquals(location, result);

        verify(locationDAO, times(1)).findById(locationId);
    }

    @Test
    void getLocationById_locationNotFound() {

        long locationId = 999L;

        when(locationDAO.findById(locationId)).thenReturn(null);

        Location result = locationService.getLocationById(locationId);

        assertNull(result);

        verify(locationDAO, times(1)).findById(locationId);
    }

    @Test
    void getAllLocations_success() {

        Location location1 = new Location();
        Location location2 = new Location();

        List<Location> locations =
                Arrays.asList(location1, location2);

        when(locationDAO.findAll()).thenReturn(locations);

        List<Location> result =
                locationService.getAllLocations();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(locations, result);

        verify(locationDAO, times(1)).findAll();
    }

    @Test
    void getAllLocations_emptyList() {

        when(locationDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Location> result =
                locationService.getAllLocations();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(locationDAO, times(1)).findAll();
    }

    @Test
    void updateLocation_success() {

        Location location = new Location();

        location.setLocationId(1L);
        location.setName("Hyderabad");
        location.setType("CITY");

        when(locationDAO.update(location)).thenReturn(true);

        boolean result =
                locationService.updateLocation(location);

        assertTrue(result);

        verify(locationDAO, times(1)).update(location);
    }

    @Test
    void updateLocation_failure() {

        Location location = new Location();

        location.setLocationId(1L);
        location.setName("Hyderabad");
        location.setType("CITY");

        when(locationDAO.update(location)).thenReturn(false);

        boolean result =
                locationService.updateLocation(location);

        assertFalse(result);

        verify(locationDAO, times(1)).update(location);
    }

    @Test
    void deleteLocation_success() {

        long locationId = 1L;

        when(locationDAO.delete(locationId)).thenReturn(true);

        boolean result =
                locationService.deleteLocation(locationId);

        assertTrue(result);

        verify(locationDAO, times(1)).delete(locationId);
    }

    @Test
    void deleteLocation_failure() {

        long locationId = 999L;

        when(locationDAO.delete(locationId)).thenReturn(false);

        boolean result =
                locationService.deleteLocation(locationId);

        assertFalse(result);

        verify(locationDAO, times(1)).delete(locationId);
    }

    // =========================
    // Exception Tests
    // =========================

    @Test
    void createLocation_nullLocation_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.createLocation(null)
                );

        assertEquals(
                "Location cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void createLocation_emptyName_throwsValidationException() {

        Location location = new Location();

        location.setName("");
        location.setType("CITY");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.createLocation(location)
                );

        assertEquals(
                "Location name cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void createLocation_emptyType_throwsValidationException() {

        Location location = new Location();

        location.setName("Hyderabad");
        location.setType("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.createLocation(location)
                );

        assertEquals(
                "Location type cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void getLocationById_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.getLocationById(0)
                );

        assertEquals(
                "Invalid location ID",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void updateLocation_nullLocation_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.updateLocation(null)
                );

        assertEquals(
                "Location cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void updateLocation_invalidId_throwsValidationException() {

        Location location = new Location();

        location.setLocationId(0L);
        location.setName("Hyderabad");
        location.setType("CITY");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.updateLocation(location)
                );

        assertEquals(
                "Invalid location ID",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void updateLocation_emptyName_throwsValidationException() {

        Location location = new Location();

        location.setLocationId(1L);
        location.setName("");
        location.setType("CITY");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.updateLocation(location)
                );

        assertEquals(
                "Location name cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void updateLocation_emptyType_throwsValidationException() {

        Location location = new Location();

        location.setLocationId(1L);
        location.setName("Hyderabad");
        location.setType("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.updateLocation(location)
                );

        assertEquals(
                "Location type cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }

    @Test
    void deleteLocation_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> locationService.deleteLocation(0)
                );

        assertEquals(
                "Invalid location ID",
                exception.getMessage()
        );

        verifyNoInteractions(locationDAO);
    }
}