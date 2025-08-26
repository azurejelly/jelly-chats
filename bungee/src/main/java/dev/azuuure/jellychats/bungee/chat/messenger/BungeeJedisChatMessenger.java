package dev.azuuure.jellychats.bungee.chat.messenger;

import com.google.gson.Gson;
import dev.azuuure.jellychats.bungee.connection.BungeeJedisPubSubHandler;
import dev.azuuure.jellychats.core.JellyChatsPlugin;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.core.messenger.impl.AbstractJedisChatMessenger;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

public class BungeeJedisChatMessenger extends AbstractJedisChatMessenger {

    private final JellyChatsPlugin plugin;
    private final ProxyServer server;
    private final Logger logger;
    private final Gson gson;

    public BungeeJedisChatMessenger(JellyChatsPlugin plugin, ProxyServer server, Logger logger) {
        this.plugin = plugin;
        this.server = server;
        this.logger = logger;
        this.gson = new Gson();
    }

    @Override
    protected Gson gson() {
        return gson;
    }

    @Override
    protected JellyChatsPlugin plugin() {
        return plugin;
    }

    @Override
    protected RedisPubSubHandler buildPubSubHandler(RedisConnection<Jedis> connection) {
        return new BungeeJedisPubSubHandler(connection, server, logger);
    }
}