package com.unclecole.dominionfun.Listeners;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.PatchBeaconObject;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if(DominionFun.getInstance().getPatchBeaconDelay().containsKey(event.getBlockPlaced().getChunk())) {

            PatchBeaconObject patchBeaconObject = DominionFun.getInstance().getPatchBeaconDelay().get(event.getBlockPlaced().getChunk());

            if(!checkCube(event.getBlockPlaced().getLocation(), patchBeaconObject.getCorner1(), patchBeaconObject.getCorner2())) {
                TL.NO_PATCH.send(event.getPlayer());
                event.setCancelled(true);
            }
        }

        if(!DominionFun.getInstance().getExoticBoneCooldown().containsKey(event.getPlayer().getUniqueId())) return;
        long time = DominionFun.getInstance().getExoticBoneCooldown().get(event.getPlayer().getUniqueId());

        if((System.currentTimeMillis()-time)/1000 < 10) {
            event.setCancelled(true);
            TL.EXOTIC_BONE_COOLDOWN.send(event.getPlayer(), new PlaceHolder("%seconds%", 10-((System.currentTimeMillis()-time)/1000)));
        }
    }

    public boolean checkCube(Location location, Location corner1, Location corner2) {
        int xOne = corner1.getBlockX();
        int xTwo = corner2.getBlockX();

        int zOne = corner1.getBlockZ();
        int zTwo = corner2.getBlockZ();

        int xLoc = location.getBlockX();
        int zLoc = location.getBlockZ();

        return !((xOne <= xLoc && xTwo >= xLoc) && (zOne <= zLoc && zTwo >= zLoc));
    }
}
