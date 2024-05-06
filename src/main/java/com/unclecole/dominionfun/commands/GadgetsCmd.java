package com.unclecole.dominionfun.commands;

import com.unclecole.dominionfun.DominionFun;
import com.unclecole.dominionfun.utils.C;
import com.unclecole.dominionfun.utils.Config;
import com.unclecole.dominionfun.utils.PlaceHolder;
import com.unclecole.dominionfun.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

public class GadgetsCmd implements CommandExecutor {

    private DominionFun plugin;
    private Config config;

    public GadgetsCmd() {
        plugin = DominionFun.getInstance();
        config = DominionFun.getInstance().getConfigUtils();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        if(!(s instanceof Player)) return false;

        InventoryGUI gui = new InventoryGUI(config.getGadgetsSize(), config.getGadgetsTitle());

        ItemBuilder placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setDurability(7)
                .setName(C.color("&7"));

        gui.fill(0, config.getGadgetsSize(), placeholder);

        config.getGadgets().forEach(gadgetsObject -> {
            gui.addButton(gadgetsObject.getSlot(), new ItemButton(gadgetsObject.getItem()) {
                @Override
                public void onClick(InventoryClickEvent e) {
                    Player player = (Player) e.getWhoClicked();
                    if(!player.hasPermission("gadgets.give")) return;

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), gadgetsObject.getCommand().replaceAll("%player%", player.getName()));
                }
            });
        });

        Player player = (Player) s;

        gui.open(player);

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
