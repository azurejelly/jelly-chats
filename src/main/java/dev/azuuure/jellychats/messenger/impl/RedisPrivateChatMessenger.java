package dev.azuuure.jellychats.messenger.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.chat.manager.ChatManager;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.connection.RedisConnection;
import dev.azuuure.jellychats.connection.impl.DefaultRedisConnection;
import dev.azuuure.jellychats.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.connection.messaging.impl.VelocityRedisPubSubHandler;
import dev.azuuure.jellychats.messenger.PrivateChatMessenger;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

public class RedisPrivateChatMessenger implements PrivateChatMessenger {

    private final Gson gson;
    private final Configuration config;
    private final ChatManager chatManager;
    private final RedisConnection<Jedis> connection;
    private final RedisPubSubHandler pubSubHandler;

    @Inject
    public RedisPrivateChatMessenger(Gson gson, Configuration config, ChatManager chatManager, ProxyServer server, Logger logger) {
        this.gson = gson;
        this.config = config;
        this.chatManager = chatManager;
        this.connection = new DefaultRedisConnection(config.getString("redis.uri", "redis://localhost:6379"));
        this.pubSubHandler = new VelocityRedisPubSubHandler(server, logger, connection);
    }

    @Override
    public void initialize() {
        connection.connect();
        chatManager.findAll()
                .stream()
                .map(PrivateChat::channel)
                .forEach(pubSubHandler::subscribe);
    }

    @Override
    public void send(CommandSource source, PrivateChat chat, String message) {
        String author = source instanceof Player player
                ? player.getUsername()
                : config.getString("console-name", "Console");

        PrivateChatMessage content = new PrivateChatMessage(author, message);
        pubSubHandler.publish(chat.channel(), gson.toJson(content));
    }

    @Override
    public void shutdown() {
        if (pubSubHandler.active()) {
            pubSubHandler.shutdown();
        }

        connection.destroy();
    }
}
