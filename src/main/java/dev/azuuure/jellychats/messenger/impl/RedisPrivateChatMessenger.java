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
import dev.azuuure.jellychats.rank.RankProvider;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.Jedis;

public class RedisPrivateChatMessenger implements PrivateChatMessenger {

    private final Gson gson;
    private final Configuration config;
    private final ChatManager chatManager;
    private final RankProvider rankProvider;
    private final RedisConnection<Jedis> connection;
    private final RedisPubSubHandler pubSubHandler;

    @Inject
    public RedisPrivateChatMessenger(
            Gson gson,
            Configuration config,
            ChatManager chatManager,
            RankProvider rankProvider,
            ProxyServer server,
            Logger logger
    ) {
        this.gson = gson;
        this.config = config;
        this.chatManager = chatManager;
        this.rankProvider = rankProvider;

        ConfigurationNode parent = config.getConfigurationNode().node("redis");
        String uri = parent.node("uri").getString();

        if (uri != null && !uri.isBlank()) {
            this.connection = new DefaultRedisConnection(uri);
        } else {
            this.connection = new DefaultRedisConnection(
                    parent.node("hostname").getString("127.0.0.1"),
                    parent.node("port").getInt(6379),
                    parent.node("username").getString(),
                    parent.node("password").getString()
            );
        }

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
