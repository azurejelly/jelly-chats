package dev.azuuure.jellychats.messenger.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.chat.manager.ChatManager;
import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.connection.RedisConnection;
import dev.azuuure.jellychats.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.connection.messaging.impl.VelocityRedisPubSubHandler;
import dev.azuuure.jellychats.messenger.PrivateChatMessenger;
import dev.azuuure.jellychats.rank.RankProvider;
import dev.azuuure.jellychats.utils.RedisUtils;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

public class RedisPrivateChatMessenger implements PrivateChatMessenger {

    @Inject private Gson gson;
    @Inject private Configuration config;
    @Inject private ChatManager chatManager;
    @Inject private RankProvider rankProvider;
    @Inject private ProxyServer server;
    @Inject private Logger logger;

    private RedisConnection<Jedis> connection;
    private RedisPubSubHandler pubSubHandler;

    @Override
    public void initialize() {
        this.connection = RedisUtils.buildFrom(config);
        this.pubSubHandler = new VelocityRedisPubSubHandler(server, logger, connection);
        this.connection.connect();
        this.pubSubHandler.initialize();
        this.chatManager.findAll()
                .stream()
                .map(PrivateChat::channel)
                .forEach(pubSubHandler::subscribe);
    }

    @Override
    public void reload() {
        this.shutdown();
        this.initialize();
    }

    @Override
    public void send(CommandSource source, PrivateChat chat, String message) {
        String author, server, prefix, suffix;

        if (source instanceof Player player) {
            prefix = rankProvider.getPrefix(player);
            suffix = rankProvider.getSuffix(player);
            author = player.getUsername();
            server = player.getCurrentServer()
                    .map(s -> s.getServerInfo().getName())
                    .orElse(config.getString("unknown-server", "N/A"));
        } else {
            prefix = "";
            suffix = "";
            author = config.getString("console-name", "Console");
            server = config.getString("console-server", "N/A");
        }

        PrivateChatMessage content = PrivateChatMessage.builder()
                .prefix(prefix)
                .suffix(suffix)
                .server(server)
                .author(author)
                .content(message)
                .build();

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
