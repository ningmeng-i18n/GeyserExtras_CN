package dev.letsgoaway.geyserextras.spigot.menus.quickmenu;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.form.BedrockForm;
import dev.letsgoaway.geyserextras.spigot.form.elements.Dropdown;

import java.util.List;

public class QuickMenuActions extends BedrockForm {
    QuickMenuActions(BedrockPlayer bplayer) {
        super("快捷菜单 操作");
        this.onClose = () -> {
            new QuickMenu(bplayer).show(bplayer);
        };
        this.onSubmit = () -> {
            new QuickMenu(bplayer).show(bplayer);
        };
        List<String> actions = Config.quickMenuCommands.keySet().stream().toList();
        add(new Dropdown("表情 #1 的操作", actions, getAction(0, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(0, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("表情 #2 的操作", actions, getAction(1, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(1, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("表情 #3 的操作", actions, getAction(2, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(2, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("表情 #4 的操作", actions, getAction(3, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(3, str);
                            bplayer.save();
                        }
                )
        );
    }

    private String getAction(int num, BedrockPlayer bedrockPlayer) {
        String name = bedrockPlayer.quickMenuActions.get(num);
        if (Config.quickMenuCommands.containsKey(name)) {
            return name;
        } else {
            return "没有";
        }
    }
}
