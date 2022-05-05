package dev.kutuptilkisi.completedaquest.gui;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class QuestGUI {

    public QuestGUI(CompleteDaQuest main, Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Quests");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

        for(int i=0; i<9; i++){
            gui.setItem(i, glass);
            gui.setItem(i+18, glass);
        }



        for(Quest quest : main.getQuests()){
            ItemStack item = new ItemStack(quest.getIcon());
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(main.color(quest.getName()));
            meta.setLore(Arrays.asList(main.color(quest.getDescription() + " &8(" + quest.getProgess(player) + "/" + quest.getCount() + ")")));

            if(quest.isCompleted(player)){
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }

            item.setItemMeta(meta);

            gui.setItem(quest.getIndex()+9, item);
        }
        player.openInventory(gui);
    }

}