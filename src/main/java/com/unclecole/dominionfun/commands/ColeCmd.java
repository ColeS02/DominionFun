package com.unclecole.dominionfun.commands;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.utils.C;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.Number;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ColeCmd implements CommandExecutor {

    private DominionFun plugin;
    private Config config;

    public ColeCmd() {
        plugin = DominionFun.getInstance();
        config = DominionFun.getInstance().getConfigUtils();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        //If its a valid integer/long then just return the number...
        if(isParsable(args[0])) {
            s.sendMessage(args[0]);
            return false;
        }

        if(hasCharacter(args[0], '.') != -1) {
            int decLoc = hasCharacter(args[0], '.');
            int typeLoc = hasType(args[0]);

            if(typeLoc == -1) {
                if(!isParsableDouble(args[0])) {
                    s.sendMessage("Invalid number");
                    return false;
                }
                s.sendMessage(args[0]);
                return true;
            }

            int beforeDec = Integer.parseInt(args[0].substring(0,decLoc));
            double afterDec = Double.parseDouble("0." + args[0].substring(decLoc+1, typeLoc));

            float total = (float) (beforeDec + afterDec) * getType(args[0]).getMulti();
            s.sendMessage(total + " ");
            return true;
        }
        if(hasType(args[0]) != -1) {
            long num = Long.parseLong(args[0].substring(0, hasType(args[0])));
            num = num * getType(args[0]).getMulti();

            s.sendMessage(num + " ");
        }


        return false;
    }

    public int hasCharacter(String inputString, char targetCharacter) {
        return inputString.indexOf(targetCharacter);
    }

    public int hasType(String inputString) {
        inputString = inputString.toLowerCase();
        if(inputString.indexOf(Number.THOUSAND.getCharacter()) != -1) return inputString.indexOf(Number.THOUSAND.getCharacter());
        if(inputString.indexOf(Number.MILLION.getCharacter()) != -1) return inputString.indexOf(Number.MILLION.getCharacter());
        if(inputString.indexOf(Number.BILLION.getCharacter()) != -1) return inputString.indexOf(Number.BILLION.getCharacter());

        return -1;
    }

    public Number getType(String inputString) {
        inputString = inputString.toLowerCase();
        if(inputString.indexOf(Number.THOUSAND.getCharacter()) != -1) return Number.THOUSAND;
        if(inputString.indexOf(Number.MILLION.getCharacter()) != -1) return Number.MILLION;
        if(inputString.indexOf(Number.BILLION.getCharacter()) != -1) return Number.BILLION;

        return null;
    }


    public boolean isParsable(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
    public boolean isParsableDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
