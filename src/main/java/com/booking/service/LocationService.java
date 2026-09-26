package com.booking.service;

import com.booking.model.Location;

import java.util.List;

public interface LocationService {

    boolean createLocation(Location location);

    Location getLocationById(long locationId);

    List<Location> getAllLocations();

    boolean updateLocation(Location location);

    boolean deleteLocation(long locationId);
}