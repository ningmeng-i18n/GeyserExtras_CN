package dev.letsgoaway.geyserextras.velocity;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GeyserEventForwarder implements EventRegistrar {
    public static final org.geysermc.geyser.api.GeyserApi api = org.geysermc.geyser.api.GeyserApi.api();

    public GeyserEventForwarder() {
        GeyserExtras.initLog.logTask("正在注册事件...", this::tryRegisterEventBus, "事件注册成功!");
    }
    public static boolean enableNetherFog = false;
    private void tryRegisterEventBus() {
        api.eventBus().subscribe(this, org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent.class, this::onClientEmoteEvent);
        try {
            GeyserEventForwarder.enableNetherFog = YamlConfigurationLoader.builder().file(api.configDirectory().resolve("config.yml").toFile()).build().load().node("above-bedrock-nether-building").getBoolean();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void onClientEmoteEvent(org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent ev) {
        Objects.requireNonNull(Objects.requireNonNull(GeyserExtras.server.getPlayer(ev.connection().javaUuid())).get().getCurrentServer().get())
                .sendPluginMessage(GeyserExtras.emoteChannel, ev.emoteId().getBytes(StandardCharsets.UTF_8));
    }
}
