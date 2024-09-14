package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ServerType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

public class GeyserExtras extends Plugin {

    public static Logger logger;

    public static InitializeLogger initLog;
    public static ProxyServer proxyServer;

    private GeyserEventForwarder forwarder;

    public static String emoteChannel = "geyserextras:emote";

    public static String fogChannel = "geyserextras:fog";
    public GeyserExtras() {
        ServerType.type = ServerType.BUNGEE;
    }

    @Override
    public void onEnable() {
        proxyServer = ProxyServer.getInstance();
        proxyServer.getPluginManager().registerListener(this, new EventListener());
        logger = this.getLogger();
        initLog = new InitializeLogger((s) -> logger.warning(s), (s) -> logger.info(s));
        initLog.start();
        PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger.warning(s));
        initLog.logTask("正在注册频道...", () -> {
            proxyServer.registerChannel(emoteChannel);
            proxyServer.registerChannel(fogChannel);
        }, "频道已注册！");
        this.forwarder = new GeyserEventForwarder();
        initLog.warn("确保你GeyserExtras配置文件中的'proxy-mode'为true");
        initLog.end();
    }
    @Override
    public void onDisable() {
        proxyServer.unregisterChannel(emoteChannel);
        proxyServer.unregisterChannel(fogChannel);
    }
}
