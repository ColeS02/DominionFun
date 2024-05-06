package com.unclecole.dominionfun.objects;

import lombok.Getter;
import redempt.redlib.itemutils.ItemBuilder;

public class GadgetsObject {

    @Getter private int slot;
    @Getter private String command;
    @Getter private ItemBuilder item;

    public GadgetsObject(int slot, String command, ItemBuilder item) {
        this.slot = slot;
        this.command = command;
        this.item = item;
    }
}
