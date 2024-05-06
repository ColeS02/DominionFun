package com.unclecole.dominionfun.Listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.*;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {

    private Config config;
    private HashMap<UUID, Long> ballCooldown;
    private ArrayList<Item> throwableCreepers;
    private HashMap<UUID, ProjectileObject> projectileMap;


    public InteractListener() {
        config = DominionFun.getInstance().getConfigUtils();
        ballCooldown = DominionFun.getInstance().getBallCooldown();
        throwableCreepers = DominionFun.getInstance().getSwitcherBalls();
        projectileMap = DominionFun.getInstance().getProjectileMap();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getItemInHand() == null) return;

        ItemStack hand = event.getPlayer().getItemInHand();

        if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;

        if(hand.getType().equals(Material.AIR)) return;

        NBTItem item = new NBTItem(hand);
        if(hand.getType().equals(config.getPatchBeacon().getType())) {
            if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
            if(event.getClickedBlock() == null) return;
            if(item.hasTag("patchbeacon")) {
                event.setCancelled(true);
                PatchBeacon(event.getPlayer(), event.getClickedBlock().getLocation(), event.getClickedBlock(), event.getBlockFace());
            }
        }
        if(hand.getType().equals(config.getMutatedPearl().getType())) {
            if (item.hasTag("mutatedpearl")) {
                if(!config.isMutatedPearlCompare()) {
                    event.setCancelled(true);
                }
                MutatedPearl(event.getPlayer());
            }
        }

        if(hand.getType().equals(config.getNinjaStar().getType())) {
            if(item.hasTag("ninjastar")) {
                event.setCancelled(true);
                NinjaStar(event.getPlayer());
            }
        }

        if(hand.getType().equals(config.getTimeWarp().getType())) {
            if(item.hasTag("timewarp")) {
                event.setCancelled(true);
                TimeWarp(event.getPlayer());
            }
        }

        if(!hand.getType().equals(config.getSwitcherBall().getType())) return;
        if(item.hasTag("switcherball")) {
            event.setCancelled(true);
            SwitcherBall(event.getPlayer());
        }
    }

    public void NinjaStar(Player player) {
        if(!DominionFun.getInstance().getLastAttacked().containsKey(player.getUniqueId())) {
            TL.HAVENT_BEEN_ATTACKED.send(player);
            return;
        }
        AttackerObject object = DominionFun.getInstance().getLastAttacked().get(player.getUniqueId());


        if(((System.currentTimeMillis() - object.getTime())/1000) > 10) {
            TL.HAVENT_BEEN_ATTACKED.send(player);
            return;
        }

        if(Bukkit.getPlayer(object.getUuid()) == null ) {
            TL.INVALID_PLAYER.send(player);
            return;
        }

        Player attacker = Bukkit.getPlayer(object.getUuid());

        ItemStack hand = player.getItemInHand();

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            if(hand.getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
            else hand.setAmount(hand.getAmount()-1);
        }

        TL.TELEPORTED_IN_2_SECONDS.send(player);
        DominionFun.getInstance().getLastAttacked().remove(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(DominionFun.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.teleport(attacker.getLocation());
                TL.TELEPORTED_TOO_LAST_ATTACKER.send(player);
            }
        }, 40);
    }

    public void TimeWarp(Player player) {
        if(!DominionFun.getInstance().getLastPearled().containsKey(player.getUniqueId())) {
            TL.HAVENT_PEARLED.send(player);
            return;
        }
        TimeWarpObject object = DominionFun.getInstance().getLastPearled().get(player.getUniqueId());


        if(((System.currentTimeMillis() - object.getTime())/1000) > 10) {
            TL.HAVENT_PEARLED.send(player);
            return;
        }

        if(object.getLocation() == null) {
            TL.INVALID_LOCATION.send(player);
            return;
        }

        ItemStack hand = player.getItemInHand();

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            if(hand.getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
            else hand.setAmount(hand.getAmount()-1);
        }

        TL.TELEPORTED_IN_2_SECONDS.send(player);
        DominionFun.getInstance().getLastPearled().remove(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(DominionFun.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.teleport(object.getLocation());
                TL.TELEPORTED_TOO_LAST_PEARL.send(player);
            }
        }, 40);
    }

    public void MutatedPearl(Player player) {
        ItemStack hand = player.getItemInHand();

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            if(hand.getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
            else hand.setAmount(hand.getAmount()-1);
        }

        EnderPearl enderPearl = player.launchProjectile(EnderPearl.class);
        enderPearl.setShooter(player);
        enderPearl.setVelocity(player.getEyeLocation().getDirection().multiply(config.getMutatedPearlVelocity()));
    }

    public void PatchBeacon(Player player, Location location, Block against, BlockFace blockFace) {
        if(DominionFun.getInstance().getPatchBeaconCooldown().containsKey(location.getChunk())) {
            long time = DominionFun.getInstance().getPatchBeaconCooldown().get(location.getChunk());

            if((System.currentTimeMillis()/1000) < time) {
                TL.PATCH_BEACON_COOLDOWN.send(player);
                return;
            }
        }

        ItemStack hand = player.getItemInHand();
        Block placed = against.getRelative(blockFace);
        BlockState state = placed.getState();

        placed.setType(config.getPatchBeacon().getType());

        BlockPlaceEvent e = new BlockPlaceEvent(placed, state, against, config.getPatchBeacon(), player, true);
        Bukkit.getPluginManager().callEvent(e);

        Location loc = against.getRelative(blockFace).getLocation();

        PatchBeaconObject patchBeaconObject = new PatchBeaconObject(loc, (System.currentTimeMillis()/1000) + 30);

        for (Chunk chunk : patchBeaconObject.getChunks()) {
            DominionFun.getInstance().getPatchBeaconDelay().put(chunk, patchBeaconObject);
            DominionFun.getInstance().getPatchBeaconCooldown().put(chunk, (System.currentTimeMillis()/1000)+30+30);
        }

        if(hand.getAmount() <= 1) {
            player.setItemInHand(new ItemStack(Material.AIR));

        } else {
            hand.setAmount(hand.getAmount()-1);
        }

        TL.PATCH_BEACON_PLACED.send(player);

        Faction faction = Board.getInstance().getFactionAt(new FLocation(placed));

        if(!faction.isSystemFaction()) {
            for (Player onlinePlayer : faction.getOnlinePlayers()) {
                TL.ENEMY_PLACED_PATCH_BEACON.send(onlinePlayer, new PlaceHolder("%xCord%", placed.getX()), new PlaceHolder("%zCord%", placed.getZ()), new PlaceHolder("%yCord%", placed.getY()));
            }
        }
    }

    public void SwitcherBall(Player player) {
        ItemStack hand = player.getItemInHand();

        if(ballCooldown.containsKey(player.getUniqueId())) {
            int time = (int) ((System.currentTimeMillis()-ballCooldown.get(player.getUniqueId()))/1000);
            if (time < config.getBallCooldown()) {
                TL.SWITCHERBALL_COOLDOWN.send(player, new PlaceHolder("%time%", config.getBallCooldown() - time));
                return;
            }
        }

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            if(hand.getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
            else hand.setAmount(hand.getAmount()-1);
        }

        NBTItem itemNBT = new NBTItem(hand);
        itemNBT.setString("player-uuid", player.getUniqueId().toString());

        ItemStack newItem = hand.clone();
        itemNBT.applyNBT(newItem);

        Item switcherBall = player.getWorld().dropItem(player.getEyeLocation(), newItem);
        switcherBall.setVelocity(player.getEyeLocation().getDirection().multiply(2));
        switcherBall.setPickupDelay(1000);

        throwableCreepers.add(switcherBall);
        ballCooldown.put(player.getUniqueId(), System.currentTimeMillis());
        projectileMap.put(player.getUniqueId(), new ProjectileObject(switcherBall, System.currentTimeMillis()/1000));
    }
}
