package dev.kutuptilkisi.completedaquest.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getInventory().getName().equals(ChatColor.DARK_BLUE + "Quests")){
            e.setCancelled(true);
        }
    }

}