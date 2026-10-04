package com.booking.service;

import com.booking.dao.RoomDAO;
import com.booking.dao.RoomDAOImpl;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class RoomServiceImpl implements RoomService {

    private static final Logger logger =
            LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomDAO roomDAO;

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    public RoomServiceImpl(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public boolean createRoom(Room room) {

        logger.info("createRoom() started");

        // Validation
        if (room == null) {
            throw new ValidationException("Room cannot be null");
        }

        if (room.getRoomNumber() == null ||
                room.getRoomNumber().trim().isEmpty()) {
            throw new ValidationException("Room number cannot be empty");
        }

        if (room.getRoomType() == null ||
                room.getRoomType().trim().isEmpty()) {
            throw new ValidationException("Room type cannot be empty");
        }

        if (room.getCapacity() <= 0) {
            throw new ValidationException("Room capacity must be greater than zero");
        }

        if (room.getBasePrice() == null ||
                room.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Base price cannot be negative");
        }

        if (room.getStatus() == null ||
                room.getStatus().trim().isEmpty()) {
            throw new ValidationException("Room status cannot be empty");
        }

        boolean created = roomDAO.create(room);

        if (created) {
            logger.info(
                    "createRoom() completed successfully. roomId={}",
                    room.getRoomId()
            );
        } else {
            logger.info("createRoom() failed");
        }

        return created;
    }

    @Override
    public Room getRoomById(long roomId) {

        logger.info(
                "getRoomById() started. roomId={}",
                roomId
        );

        // Validation
        if (roomId <= 0) {
            throw new ValidationException("Invalid room ID");
        }

        Room room = roomDAO.findById(roomId);

        if (room == null) {
            logger.info(
                    "getRoomById() completed. Room not found. roomId={}",
                    roomId
            );

            throw new ResourceNotFoundException(
                    "Room not found with ID: " + roomId
            );
        }

        logger.info(
                "getRoomById() completed successfully. roomId={}",
                roomId
        );

        return room;
    }

    @Override
    public List<Room> getAllRooms() {

        logger.info("getAllRooms() started");

        List<Room> rooms = roomDAO.findAll();

        logger.info(
                "getAllRooms() completed successfully. roomsFound={}",
                rooms.size()
        );

        return rooms;
    }

    @Override
    public boolean updateRoom(Room room) {

        logger.info("updateRoom() started");

        // Validation
        if (room == null) {
            throw new ValidationException("Room cannot be null");
        }

        logger.info(
                "updateRoom() started. roomId={}",
                room.getRoomId()
        );

        if (room.getRoomId() <= 0) {
            throw new ValidationException("Invalid room ID");
        }

        if (room.getRoomNumber() == null ||
                room.getRoomNumber().trim().isEmpty()) {
            throw new ValidationException("Room number cannot be empty");
        }

        if (room.getRoomType() == null ||
                room.getRoomType().trim().isEmpty()) {
            throw new ValidationException("Room type cannot be empty");
        }

        if (room.getCapacity() <= 0) {
            throw new ValidationException("Room capacity must be greater than zero");
        }

        if (room.getBasePrice() == null ||
                room.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Base price cannot be negative");
        }

        if (room.getStatus() == null ||
                room.getStatus().trim().isEmpty()) {
            throw new ValidationException("Room status cannot be empty");
        }

        boolean updated = roomDAO.update(room);

        if (!updated) {
            logger.info(
                    "updateRoom() failed. roomId={}",
                    room.getRoomId()
            );

            throw new ResourceNotFoundException(
                    "Room not found with ID: " + room.getRoomId()
            );
        }

        logger.info(
                "updateRoom() completed successfully. roomId={}",
                room.getRoomId()
        );

        return true;
    }

    @Override
    public boolean deleteRoom(long roomId) {

        logger.info(
                "deleteRoom() started. roomId={}",
                roomId
        );

        // Validation
        if (roomId <= 0) {
            throw new ValidationException("Invalid room ID");
        }

        boolean deleted = roomDAO.delete(roomId);

        if (!deleted) {
            logger.info(
                    "deleteRoom() failed. roomId={}",
                    roomId
            );

            throw new ResourceNotFoundException(
                    "Room not found with ID: " + roomId
            );
        }

        logger.info(
                "deleteRoom() completed successfully. roomId={}",
                roomId
        );

        return true;
    }
}