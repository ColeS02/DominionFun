package com.unclecole.dominionfun.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public enum TL {
	NO_PERMISSION("messages.no-permission", "&cYou don't have the permission to do that."),
	INVALID_ARGUMENTS("messages.invalid-arguments", "&cInvalid Arguments: <command>"),
	INVALID_PLAYER("messages.invalid-player", "&cInvalid Player"),
	INVALID_LOCATION("messages.invalid-location", "&cInvalid Location"),
	GAVE_ITEM("messages.gave-item", "&fYou've given %player% %amount% %item%!"),
	RECEIVED_ITEM("messages.recieved-item", "&fYou received %amount% %item%"),
	GAVE_SWITCHERBALL("messages.gave-switcherball", "&fYou've given %player% %amount% switcher balls!"),
	RECEIVED_SWITCHERBALL("messages.recieved-switcherball", "&fYou received %amount% switcher balls!"),
	SWITCHERBALL_COOLDOWN("messages.switcherball-cooldown", "&fCurrently on cooldown! %time%"),
	GAVE_SWITCHERSTICK("messages.gave-switcherstick", "&fYou've given %player% %amount% switcher sticks!"),
	RECEIVED_SWITCHERSTICK("messages.recieved-switcherstick", "&fYou received %amount% switcher sticks!"),
	SWITHCERSTICK_BROKE("messages.switcherstick-broke", "&4Your swithcerstick broke!"),
	SWITCHERSTICK_COOLDOWN("messages.switcherstick-cooldown", "&fCurrently on cooldown! %time%"),
	SWAPPED_PLACES("messages.swapped-places", "&fYou have swapped places"),
	GAVE_GRAPPLER("messages.gave-switcherstick", "&fYou've given %player% %amount% grappler!"),
	RECEIVED_GRAPPLER("messages.recieved-switcherstick", "&fYou received %amount% grappler!"),
	GRAPPLE_COOLDOWN("messages.grapple-cooldown", "&fCurrently on cooldown..."),
	GRAPPLE_BROKE("messages.grapple-broke", "&4Your grappler broke!"),
	HAVENT_BEEN_ATTACKED("messages.havent-been-attacked", "&cHaven't been attacked within the last 10 seconds!"),
	TELEPORTED_TOO_LAST_ATTACKER("messages.teleported-too-last-attacker", "&fYou have been teleported to your last attacker!"),
	HAVENT_PEARLED("messages.havent-pearled", "&cHaven't pearled within the last 10 seconds!"),
	TELEPORTED_TOO_LAST_PEARL("messages.teleported-too-last-pearl", "&fYou have been teleported too your last pearl!"),
	TELEPORTED_IN_2_SECONDS("messages.teleport-in-2-seconds", "&fYou will be teleported in 2 seconds!"),
	EXOTIC_BONE_COOLDOWN("messages.exotic-bone-cooldown", "&cYou have been hit by an exotic bone and can't place blocks for another %seconds% seconds!"),
	PATCH_BEACON_PLACED("messages.patch-beacon-placed", "&aSuccessfully placed patch beacon!"),
	NO_PATCH("messages.no-patch", "&cYou cant patch! Patch Beacon placed!"),
	PATCH_BEACON_COOLDOWN("messages.patch-beacon-cooldown", "&cCant place Patch Beacon while on cooldown!"),
	SUCCESSFULLY_BROKE_PATCH_BEACON("messages.successfully-broke-patch-beacon", "&aYou successfully broke patch beacon!"),
	PATCH_BEACON_NEW_DURABILITY("messages.patch-beacon-new-durability", "&aPatch beacon durability now %dura%!"),
	ENEMY_PLACED_PATCH_BEACON("messages.enemey-placed-patch-beacon", "&cEnemy has placed patch beacon at %xCord%X %yCord%Y %zCord%Z"),
	//DEATH MESSAGES
	/*FALL_DAMAGE("death-messages.fall-damage", "&fTEMP"),
	FIRE_DAMAGE("death-messages.fire-damage", "&fTEMP"),
	SUICIDE("death-messages.suicide", "&fTEMP"),
	DROWNING("death-messages.drowning", "&fTEMP"),
	CACTUS("death-messages.cactus", "&fTEMP"),*/
	NINJA_KILL("death-messages.ninja-kill", "&FTEMP"),
	SWITHCERBALL_KILL("death-messages.swithcerball-kill", "&fTEMP"),
	SWITCHERSTICK_KILL("death-messages.swithcerstick-kill", "&fTEMP"),
	EXOTICBONE_KILL("death-messages.exoticbone-kill", "&fTEMP"),
	TIMEWARP_KILL("death-messages.timewarp-kill", "&fTEMP");
	private final String path;

	private String def;
	private static ConfigFile config;

	TL(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}

	public void setDefault(String message) {
		this.def = message;
	}

	public void send(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(PlaceholderAPI.setPlaceholders(player, C.color(getDefault())));
		} else {
			sender.sendMessage(C.strip(getDefault()));
		}
	}

	public static void loadMessages(ConfigFile configFile) {
		config = configFile;
		FileConfiguration data = configFile.getConfig();
		for (TL message : values()) {
			if (!data.contains(message.getPath())) {
				data.set(message.getPath(), message.getDefault());
			}
		}
		configFile.save();
	}


	public void send(CommandSender sender, PlaceHolder... placeHolders) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(PlaceholderAPI.setPlaceholders(player, C.color(getDefault(), placeHolders)));
		} else {
			sender.sendMessage(C.strip(getDefault(), placeHolders));
		}
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(C.color(message));
	}

	public static void message(CommandSender sender, String message, PlaceHolder... placeHolders) {
		sender.sendMessage(C.color(message, placeHolders));
	}

	public static void message(CommandSender sender, List<String> message) {
		message.forEach(m -> sender.sendMessage(C.color(m)));
	}

	public static void message(CommandSender sender, List<String> message, PlaceHolder... placeHolders) {
		message.forEach(m -> sender.sendMessage(C.color(m, placeHolders)));
	}

	public static void log(Level lvl, String message) {
		Bukkit.getLogger().log(lvl, message);
	}
}
