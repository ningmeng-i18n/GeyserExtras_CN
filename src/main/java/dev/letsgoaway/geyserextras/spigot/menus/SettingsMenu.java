package dev.letsgoaway.geyserextras.spigot.menus;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.form.BedrockForm;
import dev.letsgoaway.geyserextras.spigot.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.spigot.form.elements.Toggle;

public class SettingsMenu extends BedrockForm {
    SettingsMenu(BedrockPlayer bplayer) {
        super("个性设置");
        if (Config.customCoolDownEnabled) {
            add(new Dropdown("攻击指示器", BedrockPlayer.cooldownTypes, bplayer.cooldownType,
                    bplayer::setCooldownType)
            );
        }
        if (bplayer.player.hasPermission("geyser.command.offhand")) {
            add(new Toggle("蹲下丢弃物品切换副手", bplayer.enableSneakDropOffhand, (b) -> {
                bplayer.enableSneakDropOffhand = b;
                bplayer.setEnableSneakDropOffhand(b);
            }));
        }
        add(new Toggle("箭头延迟修复", bplayer.enableArrowDelayFix, bplayer::setEnableArrowDelayFix));
    }
}
