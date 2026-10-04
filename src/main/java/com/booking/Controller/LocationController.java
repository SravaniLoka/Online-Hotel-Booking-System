package com.booking.Controller;

import com.booking.model.Location;
import com.booking.service.LocationService;
import com.booking.service.LocationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LocationController {

    private static final Logger logger =
            LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController() {
        this.locationService = new LocationServiceImpl();
    }

    public boolean createLocation(Location location) {

        logger.info("createLocation() requested");

        boolean created = locationService.createLocation(location);

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

    public Location getLocationById(long locationId) {

        logger.info(
                "getLocationById() requested. locationId={}",
                locationId
        );

        Location location = locationService.getLocationById(locationId);

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

    public List<Location> getAllLocations() {

        logger.info("getAllLocations() requested");

        List<Location> locations = locationService.getAllLocations();

        logger.info(
                "getAllLocations() completed successfully. locationsFound={}",
                locations.size()
        );

        return locations;
    }

    public boolean updateLocation(Location location) {

        logger.info(
                "updateLocation() requested. locationId={}",
                location.getLocationId()
        );

        boolean updated = locationService.updateLocation(location);

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

    public boolean deleteLocation(long locationId) {

        logger.info(
                "deleteLocation() requested. locationId={}",
                locationId
        );

        boolean deleted = locationService.deleteLocation(locationId);

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