package dev.kutuptilkisi.completedaquest.quest;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public abstract class Quest implements Listener {

    private final CompleteDaQuest main;

    private final int index;

    private final Material icon;

    private final String name;
    private final String description;

    private final Material requiredItem;
    private final EntityType requiredEntity;
    private final int count;

    private final String command;

    private final String questType;
    private final HashMap<UUID, Integer> cache;

    public Quest(String questType, CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, EntityType requiredEntity) {
        this.main = main;
        this.index = index;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.requiredEntity = requiredEntity;
        this.requiredItem = null;
        this.count = count;
        this.command = command;

        this.questType = questType;
        cache = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public Quest(String questType, CompleteDaQuest main, int index, Material icon, String name, String description, int count, String command, Material requiredItem) {
        this.main = main;
        this.index = index;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.requiredItem = requiredItem;
        this.requiredEntity = null;
        this.count = count;
        this.command = command;

        this.questType = questType;
        cache = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    /*
        GETTERS
     */

    public CompleteDaQuest getMain() {
        return main;
    }

    public int getIndex() {
        return index;
    }

    public Material getIcon(){
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EntityType getRequiredEntity() {
        return requiredEntity;
    }

    public Material getRequiredItem() {
        return requiredItem;
    }

    public int getCount() {
        return count;
    }

    public String getQuestType(){return questType;}

    public int getProgess(Player player) {
        return cache.get(player.getUniqueId());
    }

    public boolean hasProgress(Player player){
        return cache.containsKey(player.getUniqueId());
    }

    public boolean isCompleted(Player player){
        return getProgess(player) == getCount();
    }

    /*
        Others
     */

    public void addPlayerProgress(Player player){
        cache.put(player.getUniqueId(), 0);
    }

    public void addPlayerProgress(Player player, int progress){
        cache.put(player.getUniqueId(), progress);
    }

    public void increaseProgress(Player player){
        cache.put(player.getUniqueId(), getProgess(player)+1);
    }

    public void runCommand(Player player){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
    }

    /*
        Abstract
     */

    public void runQuest(Player player) {
        if(!isCompleted(player)) {
            increaseProgress(player);
            if (isCompleted(player)) {
                player.sendMessage(ChatColor.GREEN + "You have completed " + ChatColor.AQUA + getName() + ChatColor.GREEN + " Quest");
                runCommand(player);
            }
        }
    }
}