package com.unclecole.dominionfun.objects;

import lombok.Getter;

import java.util.UUID;

public class AttackerObject {

    @Getter private UUID uuid;
    @Getter private long time;


    public AttackerObject(UUID uuid, long time) {
        this.uuid = uuid;
        this.time = time;
    }
}
