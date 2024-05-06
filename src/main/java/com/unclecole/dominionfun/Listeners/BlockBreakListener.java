package com.unclecole.dominionfun.Listeners;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.PatchBeaconObject;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockBreakListener implements Listener {

    private DominionFun dominionFun;
    private HashMap<Chunk, PatchBeaconObject> patchBeaconDelay;


    public BlockBreakListener() {
        dominionFun = DominionFun.getInstance();
        patchBeaconDelay = DominionFun.getInstance().getPatchBeaconDelay();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(!patchBeaconDelay.containsKey(event.getBlock().getChunk())) return;

        PatchBeaconObject patchBeaconObject = patchBeaconDelay.get(event.getBlock().getChunk());
        Location patchLoc = patchBeaconDelay.get(event.getBlock().getChunk()).getLocation();
        Location location = event.getBlock().getLocation();

        if(location.equals(patchLoc)) {
            event.setCancelled(true);
            if(patchBeaconObject.getDurability() <= 1) {

                ArrayList<Chunk> chunks = patchBeaconObject.getChunks();

                patchBeaconObject.getLocation().getBlock().setType(Material.AIR);
                chunks.forEach(chunk -> {
                    patchBeaconDelay.remove(chunk);
                });

                TL.SUCCESSFULLY_BROKE_PATCH_BEACON.send(event.getPlayer());
                return;
            }
            patchBeaconObject.setDurability(patchBeaconObject.getDurability()-1);

            TL.PATCH_BEACON_NEW_DURABILITY.send(event.getPlayer(), new PlaceHolder("%dura%", patchBeaconObject.getDurability()));
        }


    }
}
