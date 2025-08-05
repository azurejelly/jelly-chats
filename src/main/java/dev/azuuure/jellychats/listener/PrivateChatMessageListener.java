package dev.azuuure.jellychats.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.event.PrivateChatMessageEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.ConfigurationNode;

public class PrivateChatMessageListener {

    @Inject
    private ProxyServer server;

    @Inject
    private Configuration config;

    @Subscribe
    public void onPrivateChannelMessageEvent(PrivateChatMessageEvent event) {
        PrivateChat chat = event.chat();
        PrivateChatMessage message = event.message();

        ConfigurationNode spacing = config.getConfigurationNode().node("ranks", "append-space");
        String rawPrefix = message.prefix();
        String rawSuffix = message.suffix();

        if (spacing.node("prefix").getBoolean(false)) {
            rawPrefix += " ";
        }

        if (spacing.node("suffix").getBoolean(false)) {
            rawSuffix = " " + rawSuffix;
        }

        boolean preferLegacy = config.getBoolean("ranks.prefer-legacy", false);
        Component prefix = preferLegacy
                ? LegacyComponentSerializer.legacyAmpersand().deserialize(rawPrefix)
                : MiniMessage.miniMessage().deserialize(rawPrefix);

        Component suffix = preferLegacy
                ? LegacyComponentSerializer.legacyAmpersand().deserialize(rawSuffix)
                : MiniMessage.miniMessage().deserialize(rawSuffix);

        boolean hasFormat = config.getConfigurationNode().hasChild("messages", "formatting", chat.id());
        String preferredType = hasFormat ? chat.id() : "fallback";

        Component component = config.getComponent("messages.formatting." + preferredType,
                Placeholder.unparsed("chat", chat.name()),
                Placeholder.unparsed("server", message.server()),
                Placeholder.unparsed("author", message.author()),
                Placeholder.unparsed("content", message.content()),
                Placeholder.component("prefix", prefix),
                Placeholder.component("suffix", suffix)
        );

        server.getConsoleCommandSource().sendMessage(component);
        server.getAllPlayers()
                .stream()
                .filter(player -> player.hasPermission(chat.permission()))
                .forEach(player -> player.sendMessage(component));
    }
}
