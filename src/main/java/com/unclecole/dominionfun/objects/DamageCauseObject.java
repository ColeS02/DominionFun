package com.unclecole.dominionfun.objects;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class DamageCauseObject {

    @Getter private EntityDamageEvent.DamageCause cause;
    @Getter private UUID killer;
    @Getter private long time;

    public DamageCauseObject(EntityDamageEvent.DamageCause cause, UUID killer, long time) {
        this.cause = cause;
        this.killer = killer;
        this.time = time;
    }
}
