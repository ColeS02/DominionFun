package com.unclecole.dominionfun.tasks;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.ProjectileObject;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class SwitcherBallTask {

    private HashMap<UUID, ProjectileObject> projectileMap;
    private ArrayList<Item> switcherBalls;

    public SwitcherBallTask() {
        projectileMap = DominionFun.getInstance().getProjectileMap();
        switcherBalls = DominionFun.getInstance().getSwitcherBalls();
    }

    public void runTask() {

        HashMap<Item, Location> placement = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(DominionFun.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(switcherBalls.isEmpty()) return;
                Iterator iterator = switcherBalls.iterator();

                while(iterator.hasNext()) {
                    Item entity = (Item) iterator.next();
                    if (entity.isDead()) iterator.remove();
                    if(placement.containsKey(entity)) {
                        if(Math.abs(placement.get(entity).getX() - entity.getLocation().getX()) < 0.05 && Math.abs(placement.get(entity).getZ() - entity.getLocation().getZ()) < 0.05) {
                            entity.remove();
                        }
                    }

                    placement.put(entity,entity.getLocation());
                    for(Entity e : entity.getNearbyEntities(0.25,0.25,0.25)) {
                        if(!e.getType().equals(EntityType.PLAYER)) return;
                        if(!(e instanceof Player)) continue;
                        Player player = ((Player) e).getPlayer();
                        if(projectileMap.containsKey(player.getUniqueId())) {
                            if(projectileMap.get(player.getUniqueId()).getItem().equals(entity)) {
                                continue;
                            }
                        }
                        NBTItem item = new NBTItem(entity.getItemStack());
                        if(!item.hasTag("player-uuid")) System.out.println("ERROR");
                        UUID throwerUUID = UUID.fromString(item.getString("player-uuid"));
                        Player thrower = Bukkit.getPlayer(throwerUUID);
                        Location playerLoc = player.getLocation();
                        Location throwerLoc = thrower.getLocation();

                        player.teleport(throwerLoc);
                        thrower.teleport(playerLoc);

                        TL.SWAPPED_PLACES.send(player);
                        TL.SWAPPED_PLACES.send(thrower);
                        entity.remove();
                    }
                }
            }
        },0L, 1L);
    }
}
