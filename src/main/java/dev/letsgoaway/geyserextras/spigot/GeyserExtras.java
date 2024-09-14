package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.spigot.api.APIType;
import dev.letsgoaway.geyserextras.spigot.commands.EmoteChatCommand;
import dev.letsgoaway.geyserextras.spigot.commands.GeyserExtrasCommand;
import dev.letsgoaway.geyserextras.spigot.commands.PlatformListCommand;
import dev.letsgoaway.geyserextras.spigot.commands.TabListCommand;
import dev.letsgoaway.geyserextras.spigot.parity.bedrock.EmoteUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class GeyserExtras extends JavaPlugin implements PluginMessageListener {
    public static GeyserExtras plugin;
    public static Logger logger;
    public static InitializeLogger initLog;
    public static BedrockAPI bedrockAPI;

    public GeyserExtras() {
        ServerType.type = ServerType.SPIGOT;
    }

    @Override
    public void onEnable() {
        plugin = this;
        logger = this.getLogger();
        initLog = new InitializeLogger((s) -> logger.warning(s), (s) -> logger.info(s));
        EmoteUtils.load();
        Tick.runOnNext(() -> {
            PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger.warning(s));
        });
        initLog.start();
        initLog.logTask("加载配置中...", Config::loadConfig, "配置加载成功!");
        bedrockAPI = new BedrockAPI();
        if (bedrockAPI.foundGeyserClasses) {
            StringBuilder types = new StringBuilder();
            for (APIType type : bedrockAPI.apiInstances.keySet()) {
                types.append(type.toString() + ", ");
            }
            initLog.info("API Types: " + types.substring(0, types.length() - 2));
        } else {
            initLog.warn("GeyserExtras无法初始化！这意味着Floodgate或Geyser不在您的插件文件夹中");
            initLog.endNoDone();
            this.setEnabled(false);
            return;
        }
        Objects.requireNonNull(this.getCommand("geyserextras")).setExecutor(new GeyserExtrasCommand());
        Objects.requireNonNull(this.getCommand("platformlist")).setExecutor(new PlatformListCommand());
        Objects.requireNonNull(this.getCommand("playerlist")).setExecutor(new TabListCommand());
        Objects.requireNonNull(this.getCommand("emotechat")).setExecutor(new EmoteChatCommand());
        loadGeyserOptionalPack();
        if (!getDataFolder().toPath().resolve("GeyserExtrasPack.mcpack").toFile().exists()) {
            plugin.saveResource("GeyserExtrasPack.mcpack", false);
        }
        bedrockAPI.onLoadConfig();
        initLog.logTask("注册事件中...",
                () -> getServer().getPluginManager().registerEvents(new EventListener(), this),
                "事件注册成功!"
        );
        if (Config.proxyMode) {
            initLog.logTask("正在注册代理通道...",
                    () -> {
                        getServer().getMessenger().registerIncomingPluginChannel(this, "geyserextras:emote", this);
                        getServer().getMessenger().registerOutgoingPluginChannel(this, "geyserextras:fog");
                    }, "代理频道已注册！"
            );
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::update, 0L, 0L);
        initLog.end();
    }

    public void loadGeyserOptionalPack() {
        if (!getDataFolder().toPath().resolve("GeyserOptionalPack.mcpack").toFile().exists()) {
            initLog.info("正在下载GeyserOptionalPack...");
            InputStream in = null;
            try {
                in = new URL("https://download.geysermc.org/v2/projects/geyseroptionalpack/versions/latest/builds/latest/downloads/geyseroptionalpack").openStream();
                Files.copy(in, getDataFolder().toPath().resolve("GeyserOptionalPack.mcpack"), StandardCopyOption.REPLACE_EXISTING);
                initLog.info("GeyserOptionalPack 下载成功");
            } catch (IOException ignored) {
            }
        }
    }

    public static ConcurrentHashMap<UUID, BedrockPlayer> bplayers = new ConcurrentHashMap<>();

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (bedrockAPI.isBedrockPlayer(player.getUniqueId()) && !bplayers.containsKey(player.getUniqueId())) {
                bplayers.putIfAbsent(player.getUniqueId(), new BedrockPlayer(player));
            }
        }
        bplayers.keySet().forEach((uuid) -> {
            if (!Bukkit.getOfflinePlayer(uuid).isOnline()) {
                bplayers.remove(uuid);
            }
        });
        for (BedrockPlayer bplayer : bplayers.values()) {
            bplayer.update();
        }
    }

    @Override
    public void onDisable() {
        for (BedrockPlayer bplayer : bplayers.values()) {
            bplayer.save();
            if (Config.autoReconnect) {
                bedrockAPI.reconnect(bplayer.player.getUniqueId());
            }
        }
    }

    @Override
    public void onPluginMessageReceived(@Nonnull String channel, @Nonnull Player player, @Nonnull byte[] message) {
        if (Config.proxyMode) {
            if (channel.equals("geyserextras:emote")) {
                bplayers.get(player.getUniqueId()).onPlayerEmoteEvent(new String(message, StandardCharsets.UTF_8));
            }
        }
    }
}
