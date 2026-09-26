package com.booking.service;

import com.booking.dao.LocationDAO;
import com.booking.dao.LocationDAOImpl;
import com.booking.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LocationServiceImpl implements LocationService {

    private static final Logger logger =
            LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationDAO locationDAO;

    public LocationServiceImpl() {
        this.locationDAO = new LocationDAOImpl();
    }

    @Override
    public boolean createLocation(Location location) {

        logger.info("createLocation() started");

        boolean created = locationDAO.create(location);

        if (created) {
            logger.info(
                    "createLocation() completed successfully. locationId={}",
                    location.getLocationId()
            );
        } else {
            logger.info("createLocation() failed");
        }

        return created;
    }

    @Override
    public Location getLocationById(long locationId) {

        logger.info(
                "getLocationById() started. locationId={}",
                locationId
        );

        Location location = locationDAO.findById(locationId);

        if (location != null) {
            logger.info(
                    "getLocationById() completed successfully. locationId={}",
                    locationId
            );
        } else {
            logger.info(
                    "getLocationById() completed. Location not found. locationId={}",
                    locationId
            );
        }

        return location;
    }

    @Override
    public List<Location> getAllLocations() {

        logger.info("getAllLocations() started");

        List<Location> locations = locationDAO.findAll();

        logger.info(
                "getAllLocations() completed successfully. locationsFound={}",
                locations.size()
        );

        return locations;
    }

    @Override
    public boolean updateLocation(Location location) {

        logger.info(
                "updateLocation() started. locationId={}",
                location.getLocationId()
        );

        boolean updated = locationDAO.update(location);

        if (updated) {
            logger.info(
                    "updateLocation() completed successfully. locationId={}",
                    location.getLocationId()
            );
        } else {
            logger.info(
                    "updateLocation() failed. locationId={}",
                    location.getLocationId()
            );
        }

        return updated;
    }

    @Override
    public boolean deleteLocation(long locationId) {

        logger.info(
                "deleteLocation() started. locationId={}",
                locationId
        );

        boolean deleted = locationDAO.delete(locationId);

        if (deleted) {
            logger.info(
                    "deleteLocation() completed successfully. locationId={}",
                    locationId
            );
        } else {
            logger.info(
                    "deleteLocation() failed. locationId={}",
                    locationId
            );
        }

        return deleted;
    }
}