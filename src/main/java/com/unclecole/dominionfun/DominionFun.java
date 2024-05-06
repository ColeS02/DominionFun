package com.unclecole.dominionfun;

import com.massivecraft.factions.Factions;
import com.unclecole.dominionfun.Listeners.*;
import com.unclecole.dominionfun.commands.*;
import com.unclecole.dominionfun.objects.*;
import com.unclecole.dominionfun.tasks.PatchBeaconTask;
import com.unclecole.dominionfun.tasks.SwitcherBallTask;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.ConfigFile;
import com.unclecole.dominionfun.utils.TL;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public final class DominionFun extends JavaPlugin {

    @Getter private ArrayList<Item> switcherBalls;
    @Getter private HashMap<UUID, ProjectileObject> projectileMap;
    @Getter private HashMap<UUID, Long> stickCooldown;
    @Getter private HashMap<UUID, Long> ballCooldown;
    @Getter private HashMap<UUID, Long> grappleUsers;
    @Getter private HashMap<UUID, AttackerObject> lastAttacked;
    @Getter private HashMap<UUID, TimeWarpObject> lastPearled;
    @Getter private HashMap<UUID, Long> exoticBoneCooldown;
    @Getter private HashMap<Chunk, PatchBeaconObject> patchBeaconDelay;
    @Getter private HashMap<Chunk, Long> patchBeaconCooldown;
    @Getter private Factions factions;
    private static DominionFun dominionFun;
    public static DominionFun getInstance() { return dominionFun; }
    private Config config;
    private SwitcherBallTask switcherBallTask;
    private PatchBeaconTask patchBeaconTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        dominionFun = this;
        saveDefaultConfig();
        TL.loadMessages(new ConfigFile("messages.yml", this));

        switcherBalls = new ArrayList<>();
        projectileMap = new HashMap<>();
        stickCooldown = new HashMap<>();
        ballCooldown = new HashMap<>();
        grappleUsers = new HashMap<>();
        lastAttacked = new HashMap<>();
        lastPearled = new HashMap<>();
        exoticBoneCooldown = new HashMap<>();
        patchBeaconDelay = new HashMap<>();
        patchBeaconCooldown = new HashMap<>();
        factions = Factions.getInstance();

        config = new Config(this);

        getCommand("switcherball").setExecutor(new SwitcherBallCmd());
        getCommand("switcherstick").setExecutor(new SwitcherStickCmd());
        getCommand("grappler").setExecutor(new GrapplerCmd());
        getCommand("ninjastar").setExecutor(new NinjaStarCmd());
        getCommand("timewarp").setExecutor(new TimeWarpCmd());
        getCommand("exoticbone").setExecutor(new ExoticBoneCmd());
        getCommand("gadgets").setExecutor(new GadgetsCmd());
        getCommand("mutatedpearl").setExecutor(new MutatedPearlCmd());
        getCommand("patchbeacon").setExecutor(new PatchBeaconCmd());
        /*getCommand("cole").setExecutor(new ColeCmd());*/

        this.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new AttackListener(), this);
        this.getServer().getPluginManager().registerEvents(new GrappleListener(), this);
        this.getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);


        switcherBallTask = new SwitcherBallTask();
        switcherBallTask.runTask();
        patchBeaconTask = new PatchBeaconTask();
        patchBeaconTask.runTask();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Iterator<PatchBeaconObject> iterator = patchBeaconDelay.values().iterator();
        while (iterator.hasNext()) {
            PatchBeaconObject patchBeaconObject = iterator.next();
            patchBeaconObject.getLocation().getBlock().setType(Material.AIR);
            iterator.remove();
        }
    }

    public Config getConfigUtils() { return config; }

}
