package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ReleaseVersion;
import dev.letsgoaway.geyserextras.ServerType;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public class GeyserExtras implements Extension {
    InitializeLogger initLog;

    public GeyserExtras() {
        ServerType.type = ServerType.EXTENSION;
    }

    @Subscribe
    public void onPreInitialize(GeyserPreInitializeEvent event) {
        initLog = new InitializeLogger((s) -> logger().warning(s), (s) -> logger().info(s));
        initLog.start();
        initLog.warn("GeyserExtras目前不支持作为Geyser Extension！");
        initLog.warn("如果您正在服务器/代理上运行Geyser，请将此插件");
        initLog.warn("放在您的软件的插件文件夹，而不是间歇泉扩展文件夹！");
        initLog.warn("正在禁用...");
        initLog.endNoDone();
        this.setEnabled(false);
        PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger().warning(s));
    }
}
