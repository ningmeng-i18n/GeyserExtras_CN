package dev.letsgoaway.geyserextras.spigot.commands;

import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import dev.letsgoaway.geyserextras.spigot.parity.java.tablist.TabList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())){
                new TabList(GeyserExtras.bplayers.get(player.getUniqueId()));
            }
            else {
                player.sendMessage("只有基岩玩家才能执行这个命令！");
            }
        }
        else {
            sender.sendMessage("只有基岩玩家才能执行这个命令！");
        }
        return true;
    }
}
