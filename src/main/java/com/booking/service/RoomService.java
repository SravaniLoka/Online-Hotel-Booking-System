package com.booking.service;

import com.booking.model.Room;

import java.util.List;

public interface RoomService {

    boolean createRoom(Room room);

    Room getRoomById(long roomId);

    List<Room> getAllRooms();

    boolean updateRoom(Room room);

    boolean deleteRoom(long roomId);
}