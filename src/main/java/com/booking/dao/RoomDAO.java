package com.booking.dao;

import com.booking.model.Room;

import java.sql.Connection;
import java.util.List;

public interface RoomDAO {

    boolean create(Room room);

    Room findById(long roomId);

    List<Room> findAll();

    boolean update(Room room);

    boolean update(Room room, Connection connection);

    boolean delete(long roomId);
}