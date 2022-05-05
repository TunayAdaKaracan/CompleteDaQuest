package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceQuest extends Quest {

    public PlaceQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, Material requiredItem) {
        super("PlaceQuest", main, index, icon, name, description, count, command, requiredItem);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!isCompleted(e.getPlayer())){
            if(getRequiredItem() != null && e.getBlock().getType().equals(getRequiredItem())){
                runQuest(e.getPlayer());
            } else if(getRequiredItem() == null){
                runQuest(e.getPlayer());
            }
        }
    }
}