package dev.letsgoaway.geyserextras.spigot.menus.quickmenu;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.spigot.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.spigot.form.elements.Button;
import org.geysermc.cumulus.util.FormImage;

import java.util.Arrays;

public class QuickMenuBindings extends BedrockContextMenu {
    public QuickMenuBindings(BedrockPlayer bplayer) {
        super("快捷菜单");
        this.onClose = ()->{
          new QuickMenu(bplayer).show(bplayer);
        };
        StringBuilder headerText = new StringBuilder();
        for (String s : bplayer.quickMenuList) {
            headerText.append("表情 #" + String.valueOf(bplayer.quickMenuList.indexOf(s) + 1) + " ID: " + EmoteUtils.getEmoteName(s) + "\n");
        }

        simpleForm.content(headerText.toString());
        add(new Button("设置表情 #1", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_0.png", () -> {
            bplayer.setWaiting(0);
        }));

        add(new Button("设置表情 #2", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_1.png", () -> {
            bplayer.setWaiting(1);
        }));

        add(new Button("设置表情 #3", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_2.png", () -> {
            bplayer.setWaiting(2);
        }));

        add(new Button("设置表情 #4", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_3.png", () -> {
            bplayer.setWaiting(3);
        }));
        add(new Button("清空所有", FormImage.Type.PATH, "textures/ui/icon_trash.png", () -> {
            bplayer.quickMenuList = Arrays.asList("", "", "", "");
            new QuickMenuBindings(bplayer).show(bplayer);
        }));
    }
}
