package com.booking.dao;

import com.booking.model.Hotel;
import com.booking.model.Room;
import com.booking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(RoomDAOImpl.class);

    // SQL queries
    private static final String ROOM_CREATE_SQL = """
            INSERT INTO room
            (hotel_id, room_number, room_type, capacity, base_price, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String ROOM_FIND_BY_ID_SQL = """
            SELECT room_id, hotel_id, room_number,
                   room_type, capacity, base_price, status
            FROM room
            WHERE room_id = ?
            """;

    private static final String ROOM_FIND_ALL_SQL = """
            SELECT room_id, hotel_id, room_number,
                   room_type, capacity, base_price, status
            FROM room
            ORDER BY room_id
            """;

    private static final String ROOM_UPDATE_SQL = """
            UPDATE room
            SET hotel_id = ?,
                room_number = ?,
                room_type = ?,
                capacity = ?,
                base_price = ?,
                status = ?
            WHERE room_id = ?
            """;

    private static final String ROOM_DELETE_SQL = """
            DELETE FROM room
            WHERE room_id = ?
            """;

    @Override
    public boolean create(Room room) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     ROOM_CREATE_SQL,
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

                logger.info(
                        "create() completed successfully. roomId={}",
                        room.getRoomId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating room",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public Room findById(long roomId) {

        logger.info(
                "findById() started. roomId={}",
                roomId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(ROOM_FIND_BY_ID_SQL)) {

            statement.setLong(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Room room = mapResultSetToRoom(resultSet);

                    logger.info(
                            "findById() completed successfully. roomId={}",
                            roomId
                    );

                    return room;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding room. roomId={}",
                    roomId,
                    e
            );
        }

        logger.info(
                "findById() completed. Room not found. roomId={}",
                roomId
        );

        return null;
    }

    @Override
    public List<Room> findAll() {

        logger.info("findAll() started");

        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(ROOM_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                rooms.add(
                        mapResultSetToRoom(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. roomsFound={}",
                    rooms.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all rooms",
                    e
            );
        }

        return rooms;
    }

    @Override
    public boolean update(Room room) {

        logger.info(
                "update() started. roomId={}",
                room.getRoomId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(ROOM_UPDATE_SQL)) {

            setRoomUpdateParameters(statement, room);

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. roomId={}",
                        room.getRoomId()
                );

            } else {

                logger.info(
                        "update() completed. No room updated. roomId={}",
                        room.getRoomId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating room. roomId={}",
                    room.getRoomId(),
                    e
            );
        }

        return false;
    }

    // =========================================================
    // TRANSACTION-AWARE UPDATE
    // =========================================================

    @Override
    public boolean update(
            Room room,
            Connection connection) {

        logger.info(
                "update(room, connection) started. roomId={}",
                room.getRoomId()
        );

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             ROOM_UPDATE_SQL)) {

            setRoomUpdateParameters(statement, room);

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update(room, connection) completed successfully. roomId={}",
                        room.getRoomId()
                );

            } else {

                logger.info(
                        "update(room, connection) completed. No room updated. roomId={}",
                        room.getRoomId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating room using transaction. roomId={}",
                    room.getRoomId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long roomId) {

        logger.info(
                "delete() started. roomId={}",
                roomId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(ROOM_DELETE_SQL)) {

            statement.setLong(1, roomId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. roomId={}",
                        roomId
                );

            } else {

                logger.info(
                        "delete() completed. No room deleted. roomId={}",
                        roomId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting room. roomId={}",
                    roomId,
                    e
            );
        }

        return false;
    }

    // =========================================================
    // HELPER: SET ROOM UPDATE PARAMETERS
    // =========================================================

    private void setRoomUpdateParameters(
            PreparedStatement statement,
            Room room)
            throws SQLException {

        statement.setLong(
                1,
                room.getHotel().getHotelId()
        );

        statement.setString(
                2,
                room.getRoomNumber()
        );

        statement.setString(
                3,
                room.getRoomType()
        );

        statement.setInt(
                4,
                room.getCapacity()
        );

        statement.setBigDecimal(
                5,
                room.getBasePrice()
        );

        statement.setString(
                6,
                room.getStatus()
        );

        statement.setLong(
                7,
                room.getRoomId()
        );
    }

    private Room mapResultSetToRoom(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToRoom() started");

        Hotel hotel = new Hotel();

        hotel.setHotelId(
                resultSet.getLong("hotel_id")
        );

        Room room = new Room(
                resultSet.getLong("room_id"),
                hotel,
                resultSet.getString("room_number"),
                resultSet.getString("room_type"),
                resultSet.getInt("capacity"),
                resultSet.getBigDecimal("base_price"),
                resultSet.getString("status")
        );

        logger.info(
                "mapResultSetToRoom() completed successfully. roomId={}",
                room.getRoomId()
        );

        return room;
    }
}