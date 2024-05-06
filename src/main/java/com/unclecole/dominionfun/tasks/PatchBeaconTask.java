package com.unclecole.dominionfun.tasks;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.PatchBeaconObject;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;

import java.util.HashMap;
import java.util.Iterator;

public class PatchBeaconTask {

    private HashMap<Chunk, PatchBeaconObject> patchBeaconDelay;
    private HashMap<Chunk, Long> patchBeaconCooldown;

    public PatchBeaconTask() {
        patchBeaconCooldown = DominionFun.getInstance().getPatchBeaconCooldown();
        patchBeaconDelay = DominionFun.getInstance().getPatchBeaconDelay();
    }

    public void runTask() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(DominionFun.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(patchBeaconDelay.isEmpty()) return;
                Iterator<PatchBeaconObject> iterator = patchBeaconDelay.values().iterator();

                patchBeaconCooldown.values().removeIf(time -> time < (System.currentTimeMillis() / 1000));

                while(iterator.hasNext()) {
                    PatchBeaconObject patchBeaconObject = iterator.next();

                    if(patchBeaconObject.getDespawnTime() < (System.currentTimeMillis()/1000)) {
                        Bukkit.getScheduler().runTask(DominionFun.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                patchBeaconObject.getLocation().getBlock().setType(Material.AIR);
                            }
                        });
                        iterator.remove();
                    }
                }
            }
        },0L, 20L);
    }
}
