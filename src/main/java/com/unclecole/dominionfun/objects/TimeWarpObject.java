package com.unclecole.dominionfun.objects;


import lombok.Getter;
import org.bukkit.Location;

public class TimeWarpObject {

    @Getter private Location location;
    @Getter private long time;

    public TimeWarpObject(Location location, long time) {
        this.location = location;
        this.time = time;
    }
}
