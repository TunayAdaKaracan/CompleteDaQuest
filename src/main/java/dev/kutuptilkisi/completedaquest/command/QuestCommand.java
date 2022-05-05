package dev.kutuptilkisi.completedaquest.command;

import dev.kutuptilkisi.completedaquest.CompleteDaQuest;
import dev.kutuptilkisi.completedaquest.gui.QuestGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {

    private final CompleteDaQuest main;

    public QuestCommand(CompleteDaQuest main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            new QuestGUI(main, player);
        } else {
            main.getLogger().warning("This Command Can Only Used In Game");
        }


        return false;
    }
}