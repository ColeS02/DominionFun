package com.unclecole.dominionfun.commands;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.utils.C;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GrapplerCmd implements CommandExecutor {

    private DominionFun plugin;
    private Config config;

    public GrapplerCmd() {
        plugin = DominionFun.getInstance();
        config = DominionFun.getInstance().getConfigUtils();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        if(args.length < 3) {
            TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/grappler give <name> <amount> <uses>"));
            return false;
        }
        if(args[0].equals("give")) {

            if(!s.hasPermission("grappler.give")) {
                TL.NO_PERMISSION.send(s);
                return false;
            }

            if(Bukkit.getPlayer(args[1]) == null) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/grappler give <name> <amount> <uses>"));
                return false;
            }

            if(!isParsable(args[2])) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/grappler give <name> <amount> <uses>"));
                return false;
            }

            if(!isParsable(args[3])) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/grappler give <name> <amount> <uses>"));
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);
            int amount = Integer.parseInt(args[2]);
            int uses = Integer.parseInt(args[3]);

            ItemBuilder grappler = plugin.getConfigUtils().getGrappler();

            ItemMeta meta = grappler.getItemMeta();
            List<String> lore = new ArrayList<>();

            config.getSwitcherStickLore().forEach(stg -> {
                stg = stg.replace("%uses%", String.valueOf(uses));
                lore.add(C.color(stg));
            });

            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DESTROYS,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS);
            grappler.setItemMeta(meta);

            NBTItem grapplerNBT = new NBTItem(grappler);

            grapplerNBT.setInteger("uses", uses);
            grapplerNBT.applyNBT(grappler);


            for(int i = 0; i < amount; i++) {
                grapplerNBT.setInteger("random", ThreadLocalRandom.current().nextInt(-100000, 100000));
                grapplerNBT.applyNBT(grappler);
                player.getInventory().addItem(grappler);
            }

            if(!s.equals(player)) {
                TL.GAVE_ITEM.send(s, new PlaceHolder("%player%", player.getName()), new PlaceHolder("%amount%", amount), new PlaceHolder("%item%", "Grappler"));
            }

            TL.RECEIVED_ITEM.send(player, new PlaceHolder("%amount%", amount), new PlaceHolder("%item%", "Grappler"));
        }

        return false;
    }



    public boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
