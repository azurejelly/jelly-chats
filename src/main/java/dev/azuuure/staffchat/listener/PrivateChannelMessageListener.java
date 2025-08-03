package dev.azuuure.staffchat.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.channel.message.PrivateChannelMessage;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.event.PrivateChannelMessageEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class PrivateChannelMessageListener {

    @Inject
    private ProxyServer server;

    @Inject
    private Configuration configuration;

    @Subscribe
    public void onPrivateChannelMessageEvent(PrivateChannelMessageEvent event) {
        PrivateChannel channel = event.channel();
        PrivateChannelMessage message = event.message();

        Component component = configuration.getComponent("channels.format." + channel.getType(),
                Placeholder.unparsed("author", message.author()),
                Placeholder.unparsed("content", message.content())
        );

        server.getConsoleCommandSource().sendMessage(component);
        server.getAllPlayers().forEach(player -> {
            if (!player.hasPermission(channel.getPermission())) {
                return;
            }

            player.sendMessage(component);
        });
    }
}
