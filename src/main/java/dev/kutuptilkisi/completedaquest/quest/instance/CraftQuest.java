package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftQuest extends Quest {
    public CraftQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, Material requiredItem) {
        super("CraftQuest", main, index, icon, name, description, count, command, requiredItem);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        Player player = (Player) e.getWhoClicked();
        if(!isCompleted(player)){
            if(getRequiredItem() != null && e.getRecipe().getResult().getType().equals(getRequiredItem())){
                runQuest(player);
            } else if(getRequiredItem() == null) {
                runQuest(player);
            }
        }
    }
}