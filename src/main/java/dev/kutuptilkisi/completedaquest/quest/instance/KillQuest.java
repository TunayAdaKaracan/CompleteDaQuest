package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillQuest extends Quest {

    public KillQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, EntityType requiredEntity) {
        super("KillQuest", main, index, icon, name, description, count, command, requiredEntity);
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent e){
        if(e.getEntity().getKiller() != null){
            Player player = e.getEntity().getKiller();
            if(!isCompleted(player)){
                if(getRequiredEntity() != null && e.getEntity().getType().equals(getRequiredEntity())){
                    runQuest(player);
                } else if(getRequiredEntity() == null){
                    runQuest(player);
                }
            }
        }
    }
}