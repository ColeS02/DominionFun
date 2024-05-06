package com.unclecole.dominionfun.Listeners;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.AttackerObject;
import com.unclecole.dominionfun.objects.DamageCauseObject;
import com.unclecole.dominionfun.utils.C;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AttackListener implements Listener {

    private Config config;
    private HashMap<UUID, Long> stickCooldown;
    private HashMap<UUID, Long> exoticBoneCooldown;

    public AttackListener() {
        config = DominionFun.getInstance().getConfigUtils();
        stickCooldown = DominionFun.getInstance().getStickCooldown();
        exoticBoneCooldown = DominionFun.getInstance().getExoticBoneCooldown();
    }

    @EventHandler
    public void playerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        DominionFun.getInstance().getLastAttacked().put(player.getUniqueId(), new AttackerObject(damager.getUniqueId(), System.currentTimeMillis()));

        if(damager.getInventory().getItemInHand() == null || damager.getInventory().getItemInHand().getType().equals(Material.AIR)) return;

        ItemStack hand = damager.getInventory().getItemInHand();
        NBTItem itemNBT = new NBTItem(hand);

        if(hand.getType().equals(config.getExoticBone().getType())) {
            if(itemNBT.hasTag("exoticbone")) {
                exoticBone(damager, player, itemNBT, hand);
            }
        }

        if(!hand.getType().equals(config.getSwitcherStick().getType())) return;
        if(!itemNBT.hasTag("switcherstick")) return;event.setCancelled(true);

        if(stickCooldown.containsKey(damager.getUniqueId())) {
            int time = (int) ((System.currentTimeMillis()-stickCooldown.get(damager.getUniqueId()))/1000);
            if (time < config.getStickCooldown()) {
                TL.SWITCHERSTICK_COOLDOWN.send(damager, new PlaceHolder("%time%", config.getStickCooldown() - time));
                return;
            }
        }

        itemNBT.setInteger("uses", itemNBT.getInteger("uses")-1);

        if(itemNBT.getInteger("uses") < 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
            TL.SWITHCERSTICK_BROKE.send(player);
        }

        itemNBT.applyNBT(hand);

        ItemMeta meta = hand.getItemMeta();
        List<String> lore = new ArrayList<>();

        config.getSwitcherStickLore().forEach(stg -> {
            stg = stg.replace("%uses%", String.valueOf(itemNBT.getInteger("uses")));
            lore.add(C.color(stg));
        });

        meta.setLore(lore);
        hand.setItemMeta(meta);

        Location location = player.getLocation().clone();

        if(location.getYaw() >= 0) location.setYaw(location.getYaw()-180);
        else if(location.getYaw() < 0) location.setYaw(location.getYaw()+180);

        stickCooldown.put(damager.getUniqueId(), System.currentTimeMillis());
        player.teleport(location);
    }

    public void exoticBone(Player damager, Player player, NBTItem nbtItem, ItemStack hand) {
        if(exoticBoneCooldown.containsKey(damager.getUniqueId())) {
            int time = (int) ((System.currentTimeMillis()-exoticBoneCooldown.get(damager.getUniqueId()))/1000);
            if (time < config.getExoticBoneCooldown()) {
                TL.EXOTIC_BONE_COOLDOWN.send(damager, new PlaceHolder("%time%", config.getExoticBoneCooldown() - time));
                return;
            }
        }


        nbtItem.setInteger("uses", nbtItem.getInteger("uses")-1);

        if(nbtItem.getInteger("uses") < 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
            TL.SWITHCERSTICK_BROKE.send(player);
        }

        nbtItem.applyNBT(hand);

        ItemMeta meta = hand.getItemMeta();
        List<String> lore = new ArrayList<>();

        config.getSwitcherStickLore().forEach(stg -> {
            stg = stg.replace("%uses%", String.valueOf(nbtItem.getInteger("uses")));
            lore.add(C.color(stg));
        });

        meta.setLore(lore);
        hand.setItemMeta(meta);

        DominionFun.getInstance().getExoticBoneCooldown().put(player.getUniqueId(), System.currentTimeMillis());
    }
}
