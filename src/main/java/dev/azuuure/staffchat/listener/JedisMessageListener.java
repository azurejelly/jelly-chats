package dev.azuuure.staffchat.listener;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.channel.message.PrivateChannelMessage;
import dev.azuuure.staffchat.event.JedisMessageEvent;
import dev.azuuure.staffchat.event.PrivateChannelMessageEvent;
import org.slf4j.Logger;

public class JedisMessageListener {

    @Inject
    private Gson gson;

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    @Subscribe
    public void onJedisMessageEvent(JedisMessageEvent event) {
        PrivateChannel channel = PrivateChannel.fromChannel(event.channel());
        if (channel == null) {
            return;
        }

        try {
            PrivateChannelMessage message = gson.fromJson(event.content(), PrivateChannelMessage.class);
            server.getEventManager().fireAndForget(new PrivateChannelMessageEvent(channel, message));
        } catch (JsonSyntaxException e) {
            logger.error("Received invalid message on channel '{}'", channel.getChannel(), e);
        }
    }
}
