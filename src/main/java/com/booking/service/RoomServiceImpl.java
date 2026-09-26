package com.booking.service;

import com.booking.dao.RoomDAO;
import com.booking.dao.RoomDAOImpl;
import com.booking.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomServiceImpl implements RoomService {

    private static final Logger logger =
            LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomDAO roomDAO;

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    @Override
    public boolean createRoom(Room room) {

        logger.info("createRoom() started");

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

        Room room = roomDAO.findById(roomId);

        if (room != null) {
            logger.info(
                    "getRoomById() completed successfully. roomId={}",
                    roomId
            );
        } else {
            logger.info(
                    "getRoomById() completed. Room not found. roomId={}",
                    roomId
            );
        }

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

        logger.info(
                "updateRoom() started. roomId={}",
                room.getRoomId()
        );

        boolean updated = roomDAO.update(room);

        if (updated) {
            logger.info(
                    "updateRoom() completed successfully. roomId={}",
                    room.getRoomId()
            );
        } else {
            logger.info(
                    "updateRoom() failed. roomId={}",
                    room.getRoomId()
            );
        }

        return updated;
    }

    @Override
    public boolean deleteRoom(long roomId) {

        logger.info(
                "deleteRoom() started. roomId={}",
                roomId
        );

        boolean deleted = roomDAO.delete(roomId);

        if (deleted) {
            logger.info(
                    "deleteRoom() completed successfully. roomId={}",
                    roomId
            );
        } else {
            logger.info(
                    "deleteRoom() failed. roomId={}",
                    roomId
            );
        }

        return deleted;
    }
}