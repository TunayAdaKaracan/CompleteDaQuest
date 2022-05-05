package dev.kutuptilkisi.completedaquest.quest.instance;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.quest.Quest;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Map;

public class EnchantQuest extends Quest {

    private final Enchantment enchant;
    private final Integer enchantLevel;

    public EnchantQuest(CompleteDaQuest main, int index, Material icon, String name, String description, Integer count, String command, Enchantment enchant, Integer enchantLevel, Material tool) {
        super("EnchantQuest", main, index, icon, name, description, count, command, tool);
        this.enchant = enchant;
        this.enchantLevel = enchantLevel;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e){
        Player player = e.getEnchanter();
        if(!isCompleted(player)) {

            if(getRequiredItem() != null && !e.getItem().getType().equals(getRequiredItem())) return;

            if(enchant == null){
                runQuest(player);
                return;
            }

            for (Map.Entry<Enchantment, Integer> a : e.getEnchantsToAdd().entrySet()) {
                if (a.getKey().equals(enchant) && (enchantLevel == null || (enchantLevel != null && enchantLevel.equals(a.getValue())))) {
                    runQuest(player);
                    break;
                }
            }
        }
    }
}