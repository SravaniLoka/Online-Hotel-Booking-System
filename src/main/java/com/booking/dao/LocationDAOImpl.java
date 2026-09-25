package com.booking.dao;

import com.booking.model.Location;
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

public class LocationDAOImpl implements LocationDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(LocationDAOImpl.class);

    // SQL queries
    private static final String LOCATION_CREATE_SQL = """
            INSERT INTO location
            (name, type, parent_id)
            VALUES (?, ?, ?)
            """;

    private static final String LOCATION_FIND_BY_ID_SQL = """
            SELECT location_id, name, type, parent_id
            FROM location
            WHERE location_id = ?
            """;

    private static final String LOCATION_FIND_ALL_SQL = """
            SELECT location_id, name, type, parent_id
            FROM location
            ORDER BY location_id
            """;

    private static final String LOCATION_UPDATE_SQL = """
            UPDATE location
            SET name = ?,
                type = ?,
                parent_id = ?
            WHERE location_id = ?
            """;

    private static final String LOCATION_DELETE_SQL = """
            DELETE FROM location
            WHERE location_id = ?
            """;

    @Override
    public boolean create(Location location) {

        logger.info("create() started");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     LOCATION_CREATE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getType());

            if (location.getParent() != null) {
                statement.setLong(
                        3,
                        location.getParent().getLocationId()
                );
            } else {
                statement.setNull(
                        3,
                        java.sql.Types.BIGINT
                );
            }

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        location.setLocationId(keys.getLong(1));
                    }
                }

                logger.info(
                        "create() completed successfully. locationId={}",
                        location.getLocationId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while creating location",
                    e
            );
        }

        logger.info("create() completed with failure");

        return false;
    }

    @Override
    public Location findById(long locationId) {

        logger.info(
                "findById() started. locationId={}",
                locationId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(LOCATION_FIND_BY_ID_SQL)) {

            statement.setLong(1, locationId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Location location =
                            mapResultSetToLocation(resultSet);

                    logger.info(
                            "findById() completed successfully. locationId={}",
                            locationId
                    );

                    return location;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error while finding location. locationId={}",
                    locationId,
                    e
            );
        }

        logger.info(
                "findById() completed. Location not found. locationId={}",
                locationId
        );

        return null;
    }

    @Override
    public List<Location> findAll() {

        logger.info("findAll() started");

        List<Location> locations = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(LOCATION_FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                locations.add(
                        mapResultSetToLocation(resultSet)
                );
            }

            logger.info(
                    "findAll() completed successfully. locationsFound={}",
                    locations.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Error while retrieving all locations",
                    e
            );
        }

        return locations;
    }

    @Override
    public boolean update(Location location) {

        logger.info(
                "update() started. locationId={}",
                location.getLocationId()
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(LOCATION_UPDATE_SQL)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getType());

            if (location.getParent() != null) {
                statement.setLong(
                        3,
                        location.getParent().getLocationId()
                );
            } else {
                statement.setNull(
                        3,
                        java.sql.Types.BIGINT
                );
            }

            statement.setLong(4, location.getLocationId());

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {

                logger.info(
                        "update() completed successfully. locationId={}",
                        location.getLocationId()
                );

            } else {

                logger.info(
                        "update() completed. No location updated. locationId={}",
                        location.getLocationId()
                );
            }

            return updated;

        } catch (SQLException e) {

            logger.error(
                    "Error while updating location. locationId={}",
                    location.getLocationId(),
                    e
            );
        }

        return false;
    }

    @Override
    public boolean delete(long locationId) {

        logger.info(
                "delete() started. locationId={}",
                locationId
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(LOCATION_DELETE_SQL)) {

            statement.setLong(1, locationId);

            boolean deleted = statement.executeUpdate() > 0;

            if (deleted) {

                logger.info(
                        "delete() completed successfully. locationId={}",
                        locationId
                );

            } else {

                logger.info(
                        "delete() completed. No location deleted. locationId={}",
                        locationId
                );
            }

            return deleted;

        } catch (SQLException e) {

            logger.error(
                    "Error while deleting location. locationId={}",
                    locationId,
                    e
            );
        }

        return false;
    }

    private Location mapResultSetToLocation(ResultSet resultSet)
            throws SQLException {

        logger.info("mapResultSetToLocation() started");

        Location parent = null;

        long parentId = resultSet.getLong("parent_id");

        if (!resultSet.wasNull()) {
            parent = new Location();
            parent.setLocationId(parentId);
        }

        Location location = new Location(
                resultSet.getLong("location_id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                parent
        );

        logger.info(
                "mapResultSetToLocation() completed successfully. locationId={}",
                location.getLocationId()
        );

        return location;
    }
}