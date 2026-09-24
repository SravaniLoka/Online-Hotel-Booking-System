package com.booking.dao;

import com.booking.model.Location;
import com.booking.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements LocationDAO {

    @Override
    public boolean create(Location location) {

        String sql = """
                INSERT INTO location
                (name, type, parent_id)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getType());

            if (location.getParent() != null) {
                statement.setLong(3, location.getParent().getLocationId());
            } else {
                statement.setNull(3, java.sql.Types.BIGINT);
            }

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        location.setLocationId(keys.getLong(1));
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
    public Location findById(long locationId) {

        String sql = """
                SELECT location_id, name, type, parent_id
                FROM location
                WHERE location_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, locationId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToLocation(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Location> findAll() {

        List<Location> locations = new ArrayList<>();

        String sql = """
                SELECT location_id, name, type, parent_id
                FROM location
                ORDER BY location_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                locations.add(
                        mapResultSetToLocation(resultSet)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }

    @Override
    public boolean update(Location location) {

        String sql = """
                UPDATE location
                SET name = ?,
                    type = ?,
                    parent_id = ?
                WHERE location_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getType());

            if (location.getParent() != null) {
                statement.setLong(3, location.getParent().getLocationId());
            } else {
                statement.setNull(3, java.sql.Types.BIGINT);
            }

            statement.setLong(4, location.getLocationId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(long locationId) {

        String sql = """
                DELETE FROM location
                WHERE location_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, locationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Location mapResultSetToLocation(ResultSet resultSet)
            throws SQLException {

        Location parent = null;

        long parentId = resultSet.getLong("parent_id");

        if (!resultSet.wasNull()) {
            parent = new Location();
            parent.setLocationId(parentId);
        }

        return new Location(
                resultSet.getLong("location_id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                parent
        );
    }
}