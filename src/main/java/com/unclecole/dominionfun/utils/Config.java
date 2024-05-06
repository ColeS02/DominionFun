package com.unclecole.dominionfun.utils;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.objects.GadgetsObject;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Config {
    private DominionFun dominionFun;
    @Getter private ItemBuilder switcherBall;
    @Getter private ItemBuilder switcherStick;
    @Getter private ItemBuilder grappler;
    @Getter private ItemBuilder ninjaStar;
    @Getter private ItemBuilder timeWarp;
    @Getter private ItemBuilder exoticBone;
    @Getter private ItemBuilder mutatedPearl;
    @Getter private ItemBuilder patchBeacon;


    @Getter private int stickCooldown;
    @Getter private int ballCooldown;
    @Getter private int grapplerCooldown;
    @Getter private int ninjaStarCooldown;
    @Getter private int timeWarpCooldown;
    @Getter private int exoticBoneCooldown;
    @Getter private int mutatedPearlCooldown;

    @Getter private int mutatedPearlVelocity;

    @Getter private boolean mutatedPearlCompare;

    
    @Getter private List<String> grapplerLore;
    @Getter private List<String> switcherStickLore;

    @Getter private int gadgetsSize;
    @Getter private String gadgetsTitle;
    @Getter private ArrayList<GadgetsObject> gadgets;

    public Config(DominionFun dominionFun) {
        this.dominionFun = dominionFun;
        gadgets = new ArrayList<>();
        loadSwitcherBall();
        loadSwitcherStick();
        loadConfig();
        loadGrappler();
        loadNinjaStar();
        loadTimeWarp();
        loadExoticBone();
        loadGadgets();
        loadMutatedPearl();
        loadPatchBeacon();
    }

    public void loadConfig() {
        stickCooldown = dominionFun.getConfig().getInt("SwitcherStick.Cooldown");
        ballCooldown = dominionFun.getConfig().getInt("SwitcherBall.Cooldown");
        grapplerCooldown = dominionFun.getConfig().getInt("Grappler.Cooldown");
        ninjaStarCooldown = dominionFun.getConfig().getInt("NinjaStar.Cooldown");
        timeWarpCooldown = dominionFun.getConfig().getInt("TimeWarp.Cooldown");
        exoticBoneCooldown = dominionFun.getConfig().getInt("ExoticBone.Cooldown");
        mutatedPearlCooldown = dominionFun.getConfig().getInt("MutatedPearl.Cooldown");
        mutatedPearlVelocity = dominionFun.getConfig().getInt("MutatedPearl.Velocity");
        mutatedPearlCompare = dominionFun.getConfig().getBoolean("MutatedPearl.Compare");
    }

    public void loadGadgets() {
        gadgetsSize = dominionFun.getConfig().getInt("Gadgets.GUI.Size");
        gadgetsTitle = C.color(dominionFun.getConfig().getString("Gadgets.GUI.Title"));

        ConfigurationSection gadgetSection = dominionFun.getConfig().getConfigurationSection("Gadgets.Items");

        for(String key : gadgetSection.getKeys(false)) {
            ItemBuilder gadget = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("Gadgets.Items." + key + ".Material")));
            gadget.setName(C.color(dominionFun.getConfig().getString("Gadgets.Items." + key + ".Name")));
            dominionFun.getConfig().getStringList("Gadgets.Items." + key + ".Lore").forEach(string -> {
                gadget.addLore(C.color(string));
            });

            NBTItem item = new NBTItem(gadget);

            if(gadget.getType().equals(Material.SKULL_ITEM) || gadget.getType().equals(Material.SKULL)) {
                gadget.setDurability(3);
                NBTCompound skull = item.addCompound("SkullOwner");
                skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value",  dominionFun.getConfig().getString("Gadgets.Items." + key + ".Skull"));
            }
            item.applyNBT(gadget);

            GadgetsObject object = new GadgetsObject(dominionFun.getConfig().getInt("Gadgets.Items." + key + ".Slot"), dominionFun.getConfig().getString("Gadgets.Items." + key + ".Command"),gadget);

            gadgets.add(object);
        }
    }

    public void loadSwitcherBall() {
        switcherBall = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("SwitcherBall.Item.Material")));
        switcherBall.setName(C.color(dominionFun.getConfig().getString("SwitcherBall.Item.Name")));
        dominionFun.getConfig().getStringList("SwitcherBall.Item.Lore").forEach(string -> {
            switcherBall.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(switcherBall);
        item.setInteger("switcherball", 1);

        if(switcherBall.getType().equals(Material.SKULL_ITEM) || switcherBall.getType().equals(Material.SKULL)) {
            switcherBall.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("SwitcherBall.Item.Skull"));
        }
        item.applyNBT(switcherBall);
    }

    public void loadSwitcherStick() {
        switcherStickLore = dominionFun.getConfig().getStringList("SwitcherStick.Item.Lore");

        switcherStick = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("SwitcherStick.Item.Material")));
        switcherStick.setName(C.color(dominionFun.getConfig().getString("SwitcherStick.Item.Name")));
        dominionFun.getConfig().getStringList("SwitcherStick.Item.Lore").forEach(string -> {
            switcherStick.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(switcherStick);
        item.setInteger("switcherstick", 1);

        if(switcherBall.getType().equals(Material.SKULL_ITEM) || switcherBall.getType().equals(Material.SKULL)) {
            switcherBall.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("SwitcherStick.Item.Skull"));
        }
        item.applyNBT(switcherStick);
    }
    public void loadGrappler() {
        grapplerLore = dominionFun.getConfig().getStringList("Grappler.Item.Lore");
        
        grappler = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("Grappler.Item.Material")));
        grappler.setName(C.color(dominionFun.getConfig().getString("Grappler.Item.Name")));
        dominionFun.getConfig().getStringList("Grappler.Item.Lore").forEach(string -> {
            grappler.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(grappler);
        item.setInteger("grappler", 1);

        if(grappler.getType().equals(Material.SKULL_ITEM) || grappler.getType().equals(Material.SKULL)) {
            grappler.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("Grappler.Item.Skull"));
        }
        item.applyNBT(grappler);
    }

    public void loadNinjaStar() {
        ninjaStar = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("NinjaStar.Item.Material")));
        ninjaStar.setName(C.color(dominionFun.getConfig().getString("NinjaStar.Item.Name")));
        dominionFun.getConfig().getStringList("NinjaStar.Item.Lore").forEach(string -> {
            ninjaStar.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(ninjaStar);
        item.setInteger("ninjastar", 1);

        if(ninjaStar.getType().equals(Material.SKULL_ITEM) || ninjaStar.getType().equals(Material.SKULL)) {
            ninjaStar.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("NinjaStar.Item.Skull"));
        }
        item.applyNBT(ninjaStar);
    }

    public void loadTimeWarp() {
        timeWarp = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("TimeWarp.Item.Material")));
        timeWarp.setName(C.color(dominionFun.getConfig().getString("TimeWarp.Item.Name")));
        dominionFun.getConfig().getStringList("TimeWarp.Item.Lore").forEach(string -> {
            timeWarp.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(timeWarp);
        item.setInteger("timewarp", 1);

        if(timeWarp.getType().equals(Material.SKULL_ITEM) || timeWarp.getType().equals(Material.SKULL)) {
            timeWarp.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("TimeWarp.Item.Skull"));
        }
        item.applyNBT(timeWarp);
    }

    public void loadExoticBone() {
        exoticBone = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("ExoticBone.Item.Material")));
        exoticBone.setName(C.color(dominionFun.getConfig().getString("ExoticBone.Item.Name")));
        dominionFun.getConfig().getStringList("ExoticBone.Item.Lore").forEach(string -> {
            exoticBone.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(exoticBone);
        item.setInteger("exoticbone", 1);

        if(exoticBone.getType().equals(Material.SKULL_ITEM) || exoticBone.getType().equals(Material.SKULL)) {
            exoticBone.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("ExoticBone.Item.Skull"));
        }
        item.applyNBT(exoticBone);
    }

    public void loadMutatedPearl() {
        mutatedPearl = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("MutatedPearl.Item.Material")));
        mutatedPearl.setName(C.color(dominionFun.getConfig().getString("MutatedPearl.Item.Name")));
        dominionFun.getConfig().getStringList("MutatedPearl.Item.Lore").forEach(string -> {
            mutatedPearl.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(mutatedPearl);
        item.setInteger("mutatedpearl", 1);

        if(mutatedPearl.getType().equals(Material.SKULL_ITEM) || mutatedPearl.getType().equals(Material.SKULL)) {
            mutatedPearl.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("MutatedPearl.Item.Skull"));
        }
        item.applyNBT(mutatedPearl);
    }

    public void loadPatchBeacon() {
        patchBeacon = new ItemBuilder(Material.valueOf(dominionFun.getConfig().getString("PatchBeacon.Item.Material")));
        patchBeacon.setName(C.color(dominionFun.getConfig().getString("PatchBeacon.Item.Name")));
        dominionFun.getConfig().getStringList("PatchBeacon.Item.Lore").forEach(string -> {
            patchBeacon.addLore(C.color(string));
        });

        NBTItem item = new NBTItem(patchBeacon);
        item.setInteger("patchbeacon", 1);

        if(patchBeacon.getType().equals(Material.SKULL_ITEM) || patchBeacon.getType().equals(Material.SKULL)) {
            patchBeacon.setDurability(3);
            NBTCompound skull = item.addCompound("SkullOwner");
            skull.setString("Id", "3009709f-5c0b-4434-a406-547917ae1e76");

            NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  dominionFun.getConfig().getString("PatchBeacon.Item.Skull"));
        }
        item.applyNBT(patchBeacon);
    }
}
