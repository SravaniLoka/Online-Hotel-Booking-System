package com.booking.dao;

import com.booking.model.Location;

import java.util.List;

public interface LocationDAO {

    boolean create(Location location);

    Location findById(long locationId);

    List<Location> findAll();

    boolean update(Location location);

    boolean delete(long locationId);
}