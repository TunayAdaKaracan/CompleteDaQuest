package dev.kutuptilkisi.completedaquest;

import com.mongodb.MongoConfigurationException;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import dev.kutuptilkisi.completedaquest.command.QuestCommand;
import dev.kutuptilkisi.completedaquest.listener.GUIListener;
import dev.kutuptilkisi.completedaquest.listener.JoinEvent;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import dev.kutuptilkisi.completedaquest.quest.instance.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompleteDaQuest extends JavaPlugin {

    private List<Quest> quests;

    private String connectURL;
    private String connectDatabase;
    private String connectTable;

    private MongoDatabase database;
    private MongoCollection<Document> documents;

    @Override
    public void onEnable(){
        quests = new ArrayList<>();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        if(!loadConfig()){
            getLogger().severe("::::::::::::::::::::::::::::");
            getLogger().severe("");
            getLogger().severe(":::: Config Is Empty, Disabling Plugin");
            getLogger().severe("");
            getLogger().severe("::::::::::::::::::::::::::::");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        String result = setupDatabase();

        if(!result.isEmpty()){
            getLogger().severe("::::::::::::::::::::::::::::");
            getLogger().severe("");
            getLogger().severe(":::: Incorrect Mongodb Setup, Cause: " + result);
            getLogger().severe(":::: Disabling Plugin...");
            getLogger().severe("");
            getLogger().severe("::::::::::::::::::::::::::::");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        load();

        getCommand("quest").setExecutor(new QuestCommand(this));
        Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
    }

    @Override
    public void onDisable(){
        for(Player player : Bukkit.getOnlinePlayers()){
            savePlayerCache(player);
        }
    }

    /*
        SETUP
     */

    private void load(){
        quests.add(new BreakQuest(this,0, Material.COBBLESTONE, "Break Block", "&cBreak 50 Block",50, "kill {player}", null));
        quests.add(new PlaceQuest(this, 1, Material.DIRT, "Place Block", "&aPlace 20 Block", 50, "kill {player}", null));
        quests.add(new ConsumeQuest(this, 2, Material.GOLDEN_APPLE, "Consume Golden Apple", "&aConsume 20 Golden Apple", 20, "kill {player}", Material.GOLDEN_APPLE));
        quests.add(new CraftQuest(this, 3, Material.COMPASS, "Craft A  Compass", "&bCraft a Compass", 1, "kill {player}", Material.COMPASS));
        quests.add(new WalkQuest(this, 4, Material.DIAMOND_BOOTS, "Walk", "Walk 500 Blocks", 500, "kill {player}", null));
        quests.add(new KillQuest(this, 5, Material.LEATHER, "Kill Cow", "Kill 10 Cow", 10, "kill {player}", EntityType.COW));
        quests.add(new FishQuest(this, 6, Material.RAW_FISH, "Catch Fish", "Catch 30 Fishes", 30, "kill {player}", null));
        quests.add(new BrewQuest(this, 7, Material.BREWING_STAND_ITEM, "Brew Potion", "Brew A Strength Potion", 1, "kill {player}", PotionType.STRENGTH));
        quests.add(new EnchantQuest(this, 8, Material.LAPIS_ORE, "Enchant", "Enchant A Diamond Sword With Sharpness 1", 1, "kill {player}", Enchantment.DAMAGE_ALL, 1, Material.DIAMOND_SWORD));

        for(Player player : Bukkit.getOnlinePlayers()){
            loadPlayerCache(player);
        }
    }

    private boolean loadConfig(){
        if(getConfig().getString("mongodb-connect-url").isEmpty()){
           return false;
        }

        if(getConfig().getString("mongodb-database").isEmpty()){
            return false;
        }

        if(getConfig().getString("mongodb-table").isEmpty()){
            return false;
        }

        connectURL = getConfig().getString("mongodb-connect-url");
        connectDatabase = getConfig().getString("mongodb-database");
        connectTable = getConfig().getString("mongodb-table");

        return true;
    }

    private String setupDatabase(){
        MongoClient client;
        try {
            client = MongoClients.create(connectURL);
        } catch (IllegalArgumentException ex){
            return "Connection URL Must Start With 'mongodb://' or 'mongodb+srv://";
        } catch (MongoConfigurationException mex){
            return "Connection URL Is Incorrect";
        }

        this.database = client.getDatabase(connectDatabase);
        this.documents = database.getCollection(connectTable);
        return "";
    }

    public void loadPlayerCache(Player player){
        UUID playerUUID = player.getUniqueId();

        FindIterable<Document> documents = getMongoDocuments().find(new Document("UUID", playerUUID.toString()));
        if(documents.first() == null){
            List<Document> documentList = new ArrayList<>();

            for(Quest quest : getQuests()) {
                quest.addPlayerProgress(player);
                documentList.add(new Document("quest", quest.getQuestType()).append("UUID", playerUUID.toString()).append("progress", 0));
            }
            getMongoDocuments().insertMany(documentList);
        } else {
            documents.forEach((document -> {
                getQuest(document.getString("quest")).addPlayerProgress(player, document.getInteger("progress"));
            }));
        }
    }

    public void savePlayerCache(Player player){
        UUID playerUUID = player.getUniqueId();

        FindIterable<Document> documents = getMongoDocuments().find(new Document("UUID", playerUUID.toString()));

        documents.forEach((document -> {
            getMongoDocuments().updateOne(document, Updates.combine(
                    Updates.set("progress", getQuest(document.getString("quest")).getProgess(player))
            ));
        }));
    }

    /*
        Database
     */

    public MongoDatabase getMongoDatabase() {
        return database;
    }

    public MongoCollection<Document> getMongoDocuments() {
        return documents;
    }

    /*
                QUESTS
    */
    public Quest getQuest(String name){
        for(Quest quest : quests){
            if(quest.getQuestType().equalsIgnoreCase(name)){
                return quest;
            }
        }
        return null;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    /*
        UTILITY
     */

    public String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}