package com.unclecole.dominionfun.commands;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MutatedPearlCmd implements CommandExecutor {

    private DominionFun plugin;

    public MutatedPearlCmd() {
        plugin = DominionFun.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        if(args.length < 3) {
            TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/mutatedpearl give <name> <amount>"));
            return false;
        }
        if(args[0].equals("give")) {

            if(!s.hasPermission("mutatedpearl.give")) {
                TL.NO_PERMISSION.send(s);
                return false;
            }

            if(Bukkit.getPlayer(args[1]) == null) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/mutatedpearl give <name> <amount>"));
                return false;
            }

            if(!isParsable(args[2])) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/mutatedpearl give <name> <amount>"));
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);
            int amount = Integer.parseInt(args[2]);

            for(int i = 0; i < amount; i++) {
                player.getInventory().addItem(plugin.getConfigUtils().getMutatedPearl());
            }

            if(!s.equals(player)) {
                TL.GAVE_ITEM.send(s, new PlaceHolder("%player%", player.getName()), new PlaceHolder("%amount%", amount), new PlaceHolder("%item%", "Mutated Pearl"));
            }

            TL.RECEIVED_ITEM.send(player, new PlaceHolder("%amount%", amount), new PlaceHolder("%item%", "Mutated Pearl"));
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
