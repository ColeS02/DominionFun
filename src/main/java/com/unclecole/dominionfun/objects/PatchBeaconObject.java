package com.unclecole.dominionfun.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;

public class PatchBeaconObject {

    @Getter private ArrayList<Chunk> chunks;
    @Getter private long despawnTime;
    @Getter private Location location;
    @Getter private Location corner1;
    @Getter private Location corner2;
    @Getter @Setter private int durability;

    public PatchBeaconObject(Location location, long despawnTime) {
        this.location = location;
        this.despawnTime = despawnTime;
        this.corner1 = location.clone().add(-5,0,-5);
        this.corner2 = location.clone().add(5,0,5);

        durability = 5;

        chunks = new ArrayList<>();

        chunks.add(corner1.getChunk());
        chunks.add(corner2.getChunk());
        chunks.add(location.clone().add(-5,0,5).getChunk());
        chunks.add(location.clone().add(5,0,-5).getChunk());
    }
}
