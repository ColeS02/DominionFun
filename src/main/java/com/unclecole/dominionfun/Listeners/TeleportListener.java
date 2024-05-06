package com.unclecole.dominionfun.Listeners;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.TimeWarpObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!(event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL))) return;

        Player player = event.getPlayer();

        if(event.isCancelled()) return;

        DominionFun.getInstance().getLastPearled().put(player.getUniqueId(), new TimeWarpObject(event.getTo(), System.currentTimeMillis()));

    }


}
