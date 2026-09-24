package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public boolean create(Room room) {

        String sql = """
                INSERT INTO room
                (hotel_id, room_number, room_type, capacity, base_price, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, room.getHotel().getHotelId());
            statement.setString(2, room.getRoomNumber());
            statement.setString(3, room.getRoomType());
            statement.setInt(4, room.getCapacity());
            statement.setBigDecimal(5, room.getBasePrice());
            statement.setString(6, room.getStatus());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        room.setRoomId(keys.getLong(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Room findById(long roomId) {

        String sql = """
                SELECT room_id, hotel_id, room_number,
                       room_type, capacity, base_price, status
                FROM room
                WHERE room_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToRoom(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Room> findAll() {

        List<Room> rooms = new ArrayList<>();

        String sql = """
                SELECT room_id, hotel_id, room_number,
                       room_type, capacity, base_price, status
                FROM room
                ORDER BY room_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                rooms.add(
                        mapResultSetToRoom(resultSet)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    @Override
    public boolean update(Room room) {

        String sql = """
                UPDATE room
                SET hotel_id = ?,
                    room_number = ?,
                    room_type = ?,
                    capacity = ?,
                    base_price = ?,
                    status = ?
                WHERE room_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, room.getHotel().getHotelId());
            statement.setString(2, room.getRoomNumber());
            statement.setString(3, room.getRoomType());
            statement.setInt(4, room.getCapacity());
            statement.setBigDecimal(5, room.getBasePrice());
            statement.setString(6, room.getStatus());
            statement.setLong(7, room.getRoomId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long roomId) {

        String sql = """
                DELETE FROM room
                WHERE room_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roomId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Room mapResultSetToRoom(ResultSet resultSet)
            throws SQLException {

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        return new Room(
                resultSet.getLong("room_id"),
                hotel,
                resultSet.getString("room_number"),
                resultSet.getString("room_type"),
                resultSet.getInt("capacity"),
                resultSet.getBigDecimal("base_price"),
                resultSet.getString("status")
        );
    }
}