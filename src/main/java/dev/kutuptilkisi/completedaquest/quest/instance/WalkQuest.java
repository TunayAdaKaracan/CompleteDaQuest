package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class WalkQuest extends Quest {
    private final HashMap<UUID, Location> locs;

    public WalkQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, Material requiredItem) {
        super("WalkQuest", main, index, icon, name, description, count, command, requiredItem);

        this.locs = new HashMap<>();
    }

    @Override
    public void runQuest(Player player){
        increaseProgress(player);
        if(isCompleted(player)){
            player.sendMessage(ChatColor.GREEN + "You have completed " + ChatColor.AQUA + getName() + ChatColor.GREEN + " Quest");
            runCommand(player);
            locs.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(!isCompleted(e.getPlayer())){
            if(!locs.containsKey(e.getPlayer().getUniqueId())){
                locs.put(e.getPlayer().getUniqueId(), e.getTo());
            } else {
                if(locs.get(e.getPlayer().getUniqueId()).distance(e.getTo()) > 1){
                    locs.put(e.getPlayer().getUniqueId(), e.getTo());
                    this.runQuest(e.getPlayer());
                }
            }
        }
    }

}