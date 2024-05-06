package com.unclecole.dominionfun.objects;

import org.bukkit.entity.Item;

public class ProjectileObject {

    private Item item;
    private long cooldown;

    public ProjectileObject(Item item, long cooldown) {
        this.item = item;
        this.cooldown = cooldown;
    }

    public Item getItem() { return item; }
}
