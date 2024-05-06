package com.unclecole.dominionfun.Listeners;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.events.PlayerGrappleEvent;
import com.unclecole.dominionfun.utils.C;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.*;

public class GrappleListener implements Listener {

    private HashMap<UUID, Long> grappleUsers;
    private Config config;

    public GrappleListener() {
        grappleUsers = DominionFun.getInstance().getGrappleUsers();
        config = DominionFun.getInstance().getConfigUtils();

    }

    @EventHandler
    public void fishEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Location location = event.getHook().getLocation();

        NBTItem item = new NBTItem(event.getPlayer().getInventory().getItemInHand());
        if(!item.hasTag("grappler")) return;

        if(!event.getState().equals(PlayerFishEvent.State.FISHING)) {
            PlayerGrappleEvent grappleEvent = new PlayerGrappleEvent(player, player, location);
            Bukkit.getServer().getPluginManager().callEvent(grappleEvent);
            grappleUsers.putIfAbsent(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGrapple(PlayerGrappleEvent event) {

        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        ItemStack grappler = player.getItemInHand();

        if(grappleUsers.containsKey(player.getUniqueId())) {
            int time = (int) ((System.currentTimeMillis()-grappleUsers.get(player.getUniqueId()))/1000);
            if (time < config.getBallCooldown()) {
                TL.SWITCHERBALL_COOLDOWN.send(player, new PlaceHolder("%time%", config.getBallCooldown() - time));
                return;
            }
        }

        Entity e = event.getPulledEntity();
        Location loc = event.getPullLocation();
        NBTItem grapplerNBT = new NBTItem(grappler);

        if (player.equals(e)) {
            grapplerNBT.setInteger("uses", grapplerNBT.getInteger("uses")-1);
            pullPlayerSlightly(player, loc);
            //loc.getWorld().playSound(loc, Sound.BLOCK_SLIME_FALL, 1f, 1f);
        }
        if(grapplerNBT.getInteger("uses") < 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
            TL.GRAPPLE_BROKE.send(player);
        }

        grapplerNBT.applyNBT(grappler);

        ItemMeta meta = grappler.getItemMeta();
        List<String> lore = new ArrayList<>();

        config.getSwitcherStickLore().forEach(stg -> {
            stg = stg.replace("%uses%", String.valueOf(grapplerNBT.getInteger("uses")));
            lore.add(C.color(stg));
        });

        meta.setLore(lore);
        grappler.setItemMeta(meta);

    }

    private void pullPlayerSlightly(Player p, Location loc) {

        Location playerLoc = p.getLocation();

        Vector vector = loc.toVector().subtract(playerLoc.toVector()).normalize();
        p.setVelocity(vector.multiply(1.5));

    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent event) {
        if(event.getItem().getType().equals(Material.FISHING_ROD)) {
            event.setCancelled(true);
        }
    }

    private boolean searchLocation(Location location) {
        if(!location.clone().add(0.5,0.0,0.0).getBlock().getType().equals(Material.AIR)) return true;
        if(!location.clone().add(-0.5,0.0,0.0).getBlock().getType().equals(Material.AIR)) return true;
        if(!location.clone().add(0.0,0.5,0.0).getBlock().getType().equals(Material.AIR)) return true;
        if(!location.clone().add(0.0,-0.5,0.0).getBlock().getType().equals(Material.AIR)) return true;
        if(!location.clone().add(0.0,0.0,0.5).getBlock().getType().equals(Material.AIR)) return true;
        if(!location.clone().add(0.0,0.0,-0.5).getBlock().getType().equals(Material.AIR)) return true;
        return false;
    }
}
