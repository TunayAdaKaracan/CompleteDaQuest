package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BrewQuest extends Quest {
    private final HashMap<Location, UUID> openBrewingStands;
    private final PotionType type;

    public BrewQuest(CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, PotionType type) {
        super("BrewQuest", main, index, icon, name, description, count, command, (Material) null);

        this.type = type;

        this.openBrewingStands = new HashMap<>();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BREWING_STAND)){
            openBrewingStands.put(e.getClickedBlock().getLocation(), e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBrew(BrewEvent e){

        Player player = null;

        Location loc = e.getBlock().getLocation();

        for(Map.Entry<Location, UUID> entry : openBrewingStands.entrySet()){
            if(entry.getKey().equals(loc)){
                player = Bukkit.getPlayer(entry.getValue());
                break;
            }
        }
        if(player == null){
            return;
        }

        Player finalPlayer = player;

        openBrewingStands.remove(loc);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isCompleted(finalPlayer)){
                    Potion potion_1 = null;
                    Potion potion_2 = null;
                    Potion potion_3 = null;
                    if(e.getContents().getItem(0) != null && Potion.fromItemStack(e.getContents().getItem(0)).getType() != null) {
                        potion_1 = Potion.fromItemStack(e.getContents().getItem(0));
                    }
                    if(e.getContents().getItem(1) != null && Potion.fromItemStack(e.getContents().getItem(1)).getType() != null) {
                        potion_2 = Potion.fromItemStack(e.getContents().getItem(1));
                    }
                    if(e.getContents().getItem(2) != null && Potion.fromItemStack(e.getContents().getItem(2)).getType() != null) {
                        potion_3 = Potion.fromItemStack(e.getContents().getItem(2));
                    }

                    if(type != null){
                        if(potion_1 != null && potion_1.getType().equals(type)){
                            runQuest(finalPlayer);
                        }
                        if(potion_2 != null && potion_2.getType().equals(type)){
                            runQuest(finalPlayer);
                        }
                        if(potion_3 != null && potion_3.getType().equals(type)){
                            runQuest(finalPlayer);
                        }

                    } else{
                        if(potion_1 != null){
                            runQuest(finalPlayer);
                        }
                        if(potion_2 != null){
                            runQuest(finalPlayer);
                        }
                        if(potion_3 != null){
                            runQuest(finalPlayer);
                        }
                    }
                }
            }
        }.runTaskLater(getMain(), 1L);
    }

}