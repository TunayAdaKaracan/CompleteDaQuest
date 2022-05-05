package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumeQuest extends Quest {

    public ConsumeQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, Material requiredItem) {
        super("ConsumeQuest", main, index, icon, name, description, count, command, requiredItem);
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e){
        if(!isCompleted(e.getPlayer())){
            if(getRequiredItem() != null && e.getItem().getType().equals(getRequiredItem())){
                runQuest(e.getPlayer());
            } else if(getRequiredItem() == null){
                runQuest(e.getPlayer());
            }
        }
    }
}