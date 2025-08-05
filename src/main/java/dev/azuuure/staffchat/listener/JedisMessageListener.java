package dev.azuuure.staffchat.listener;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.chat.message.PrivateChatMessage;
import dev.azuuure.staffchat.chat.PrivateChat;
import dev.azuuure.staffchat.chat.manager.ChatManager;
import dev.azuuure.staffchat.event.JedisMessageEvent;
import dev.azuuure.staffchat.event.PrivateChatMessageEvent;
import org.slf4j.Logger;

public class JedisMessageListener {

    @Inject
    private Gson gson;

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    @Inject
    private ChatManager chatManager;

    @Subscribe
    public void onJedisMessageEvent(JedisMessageEvent event) {
        PrivateChat chat = chatManager.fromChannel(event.channel());
        if (chat == null) {
            return;
        }

        try {
            PrivateChatMessage message = gson.fromJson(event.content(), PrivateChatMessage.class);
            server.getEventManager().fireAndForget(new PrivateChatMessageEvent(chat, message));
        } catch (JsonSyntaxException e) {
            logger.error("Received invalid message on channel '{}'", event.channel(), e);
        }
    }
}
