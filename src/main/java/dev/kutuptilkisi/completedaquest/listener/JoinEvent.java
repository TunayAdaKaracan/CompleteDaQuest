package dev.kutuptilkisi.completedaquest.listener;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener {

    private final CompleteDaQuest main;

    public JoinEvent(CompleteDaQuest main){
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        main.loadPlayerCache(e.getPlayer());

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        main.savePlayerCache(e.getPlayer());
    }


}