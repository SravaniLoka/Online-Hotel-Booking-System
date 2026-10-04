package com.booking.Controller;

import com.booking.model.Room;
import com.booking.service.RoomService;
import com.booking.service.RoomServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomController {

    private static final Logger logger =
            LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController() {
        this.roomService = new RoomServiceImpl();
    }

    public boolean createRoom(Room room) {

        logger.info("createRoom() requested");

        boolean created = roomService.createRoom(room);

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

    public Room getRoomById(long roomId) {

        logger.info(
                "getRoomById() requested. roomId={}",
                roomId
        );

        Room room = roomService.getRoomById(roomId);

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

    public List<Room> getAllRooms() {

        logger.info("getAllRooms() requested");

        List<Room> rooms = roomService.getAllRooms();

        logger.info(
                "getAllRooms() completed successfully. roomsFound={}",
                rooms.size()
        );

        return rooms;
    }

    public boolean updateRoom(Room room) {

        logger.info(
                "updateRoom() requested. roomId={}",
                room.getRoomId()
        );

        boolean updated = roomService.updateRoom(room);

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

    public boolean deleteRoom(long roomId) {

        logger.info(
                "deleteRoom() requested. roomId={}",
                roomId
        );

        boolean deleted = roomService.deleteRoom(roomId);

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