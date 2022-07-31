package de.bizepus.constructor.utils;

import org.bukkit.Location;

public class LocationSet {
    private Location first;
    private Location second;

    public LocationSet(Location location1, Location location2) {
        this.first = location1;
        this.second = location2;
    }

    public Location getFirst() {
        return this.first;
    }

    public Location getSecond() {
        return this.second;
    }

    public void setFirst(Location loc) {
        this.first = loc;
    }

    public void setSecond(Location loc) {
        this.second = loc;
    }
}
