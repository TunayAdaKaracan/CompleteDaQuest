package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public class FishQuest extends Quest {
    public FishQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, EntityType requiredEntity) {
        super("FishQuest", main, index, icon, name, description, count, command, requiredEntity);
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent e){
        if(!isCompleted(e.getPlayer()) && e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) && ((Item) e.getCaught()).getItemStack().getType().equals(Material.RAW_FISH)){
            runQuest(e.getPlayer());
        }
    }
}