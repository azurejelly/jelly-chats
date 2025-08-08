package dev.azuuure.jellychats.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.event.PrivateChatMessageEvent;
import dev.azuuure.jellychats.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.spongepowered.configurate.ConfigurationNode;

import static dev.azuuure.jellychats.utils.ChatFormatUtil.DEFAULT_FALLBACK;

public class PrivateChatMessageListener {

    @Inject
    private ProxyServer server;

    @Inject
    private Configuration config;

    @Subscribe
    public void onPrivateChannelMessageEvent(PrivateChatMessageEvent event) {
        PrivateChat chat = event.chat();
        PrivateChatMessage message = event.message();

        ConfigurationNode spacing = config.getRootNode().node("ranks", "append-space");
        String prefix = message.prefix();
        String suffix = message.suffix();

        if (spacing.node("prefix").getBoolean(false)) {
            prefix += " ";
        }

        if (spacing.node("suffix").getBoolean(false)) {
            suffix = " " + suffix;
        }

        boolean preferLegacy = config.getBoolean("ranks.prefer-legacy", false);
        String format = config.getRootNode().hasChild("messages", "formatting", chat.id())
                ? "messages.formatting." + chat.id()
                : "messages.formatting.fallback";

        Component component = config.getComponent(format, DEFAULT_FALLBACK,
                Placeholder.unparsed("chat", chat.name()),
                Placeholder.unparsed("server", message.server()),
                Placeholder.unparsed("author", message.author()),
                Placeholder.unparsed("content", message.content()),
                Placeholder.component("prefix", ComponentUtil.toComponent(prefix, preferLegacy)),
                Placeholder.component("suffix", ComponentUtil.toComponent(suffix, preferLegacy))
        );

        server.getConsoleCommandSource().sendMessage(component);
        server.getAllPlayers()
                .stream()
                .filter(player -> player.hasPermission(chat.permission()))
                .forEach(player -> player.sendMessage(component));
    }
}
