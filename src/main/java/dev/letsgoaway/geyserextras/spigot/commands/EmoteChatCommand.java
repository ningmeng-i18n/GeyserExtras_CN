package dev.letsgoaway.geyserextras.spigot.commands;

import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EmoteChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())) {
                setEnabled(player, !getEnabled(player));
                if (getEnabled(player)){
                    player.sendRawMessage("表情聊天已开启");
                }
                else {
                    player.sendRawMessage("表情聊天已开启");
                }
            }
            else {
                player.sendRawMessage("您可以在“聊天”菜单中将表情聊天关闭");
            }
        }
        return true;
    }

    public static boolean getEnabled(Player player) {
        if (hasData("表情聊天", player)) {
          return getData("表情聊天", PersistentDataType.BOOLEAN, player);
        }
        return Config.muteEmoteChat;
    }
    public static void setEnabled(Player player, boolean b) {
        setData("表情聊天", PersistentDataType.BOOLEAN, b, player);
    }
    private static PersistentDataContainer playerSaveData(Player player) {
        return player.getPersistentDataContainer();
    }

    private static boolean hasData(String key, Player player) {
        try {
            return playerSaveData(player).has(NamespacedKey.fromString(key, GeyserExtras.plugin));
        } catch (Exception ignored) {
            return false;
        }
    }

    private static <P, C> void setData(String key, PersistentDataType<P, C> type, C value, Player player) {
        playerSaveData(player).set(NamespacedKey.fromString(key, GeyserExtras.plugin), type, value);
    }


    private static <P, C> C getData(String key, PersistentDataType<P, C> type, Player player) {
        return playerSaveData(player).get(NamespacedKey.fromString(key, GeyserExtras.plugin), type);
    }
}
