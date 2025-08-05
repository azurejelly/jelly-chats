package dev.azuuure.jellychats.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.event.PrivateChatMessageEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class PrivateChatMessageListener {

    @Inject
    private ProxyServer server;

    @Inject
    private Configuration configuration;

    @Subscribe
    public void onPrivateChannelMessageEvent(PrivateChatMessageEvent event) {
        PrivateChat chat = event.chat();
        PrivateChatMessage message = event.message();

        boolean hasFormat = configuration.getConfigurationNode().hasChild("messages", "formatting", chat.id());
        String preferredType = hasFormat ? chat.id() : "fallback";

        Component component = configuration.getComponent("messages.formatting." + preferredType,
                Placeholder.unparsed("chat", chat.name()),
                Placeholder.unparsed("author", message.author()),
                Placeholder.unparsed("content", message.content())
        );

        server.getConsoleCommandSource().sendMessage(component);
        server.getAllPlayers().forEach(player -> {
            if (!player.hasPermission(chat.permission())) {
                return;
            }

            player.sendMessage(component);
        });
    }
}
