package com.booking.test;

import com.booking.dao.RoomDAO;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Room;
import com.booking.service.RoomServiceImpl;
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
class RoomServiceImplTest {

    @Mock
    private RoomDAO roomDAO;

    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomServiceImpl(roomDAO);
    }

    // =========================
    // CREATE ROOM
    // =========================

    @Test
    void createRoom_success() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        when(roomDAO.create(room)).thenReturn(true);

        boolean result = roomService.createRoom(room);

        assertTrue(result);

        verify(roomDAO, times(1)).create(room);
    }

    @Test
    void createRoom_failure() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        when(roomDAO.create(room)).thenReturn(false);

        boolean result = roomService.createRoom(room);

        assertFalse(result);

        verify(roomDAO, times(1)).create(room);
    }

    // =========================
    // GET ROOM BY ID
    // =========================

    @Test
    void getRoomById_roomFound() {

        long roomId = 1L;
        Room room = new Room();

        when(roomDAO.findById(roomId)).thenReturn(room);

        Room result = roomService.getRoomById(roomId);

        assertNotNull(result);
        assertEquals(room, result);

        verify(roomDAO, times(1)).findById(roomId);
    }

    @Test
    void getRoomById_roomNotFound() {

        long roomId = 999L;

        when(roomDAO.findById(roomId)).thenReturn(null);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> roomService.getRoomById(roomId)
                );

        assertEquals(
                "Room not found with ID: 999",
                exception.getMessage()
        );

        verify(roomDAO, times(1)).findById(roomId);
    }

    // =========================
    // GET ALL ROOMS
    // =========================

    @Test
    void getAllRooms_success() {

        Room room1 = new Room();
        Room room2 = new Room();

        List<Room> rooms =
                Arrays.asList(room1, room2);

        when(roomDAO.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(rooms, result);

        verify(roomDAO, times(1)).findAll();
    }

    @Test
    void getAllRooms_emptyList() {

        when(roomDAO.findAll())
                .thenReturn(Collections.emptyList());

        List<Room> result = roomService.getAllRooms();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roomDAO, times(1)).findAll();
    }

    // =========================
    // UPDATE ROOM
    // =========================

    @Test
    void updateRoom_success() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        when(roomDAO.update(room)).thenReturn(true);

        boolean result = roomService.updateRoom(room);

        assertTrue(result);

        verify(roomDAO, times(1)).update(room);
    }

    @Test
    void updateRoom_failure() {

        Room room = new Room();

        room.setRoomId(999L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        when(roomDAO.update(room)).thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Room not found with ID: 999",
                exception.getMessage()
        );

        verify(roomDAO, times(1)).update(room);
    }

    // =========================
    // DELETE ROOM
    // =========================

    @Test
    void deleteRoom_success() {

        long roomId = 1L;

        when(roomDAO.delete(roomId)).thenReturn(true);

        boolean result = roomService.deleteRoom(roomId);

        assertTrue(result);

        verify(roomDAO, times(1)).delete(roomId);
    }

    @Test
    void deleteRoom_failure() {

        long roomId = 999L;

        when(roomDAO.delete(roomId)).thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> roomService.deleteRoom(roomId)
                );

        assertEquals(
                "Room not found with ID: 999",
                exception.getMessage()
        );

        verify(roomDAO, times(1)).delete(roomId);
    }

    // =========================
    // VALIDATION EXCEPTIONS
    // =========================

    @Test
    void createRoom_nullRoom_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(null)
                );

        assertEquals(
                "Room cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_emptyRoomNumber_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Room number cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_emptyRoomType_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Room type cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_invalidCapacity_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(0);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Room capacity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_nullBasePrice_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(null);
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Base price cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_negativeBasePrice_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("-100.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Base price cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void createRoom_emptyStatus_throwsValidationException() {

        Room room = new Room();

        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.createRoom(room)
                );

        assertEquals(
                "Room status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void getRoomById_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.getRoomById(0)
                );

        assertEquals(
                "Invalid room ID",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_nullRoom_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(null)
                );

        assertEquals(
                "Room cannot be null",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_invalidId_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(0L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Invalid room ID",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_emptyRoomNumber_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Room number cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_emptyRoomType_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Room type cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_invalidCapacity_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(0);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Room capacity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_nullBasePrice_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(null);
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Base price cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_negativeBasePrice_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("-100.00"));
        room.setStatus("AVAILABLE");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Base price cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void updateRoom_emptyStatus_throwsValidationException() {

        Room room = new Room();

        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("2500.00"));
        room.setStatus("");

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.updateRoom(room)
                );

        assertEquals(
                "Room status cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }

    @Test
    void deleteRoom_invalidId_throwsValidationException() {

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> roomService.deleteRoom(0)
                );

        assertEquals(
                "Invalid room ID",
                exception.getMessage()
        );

        verifyNoInteractions(roomDAO);
    }
}