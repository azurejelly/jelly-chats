package dev.azuuure.jellychats.bungee.connection;

import dev.azuuure.jellychats.bungee.event.JedisMessageEvent;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.messaging.AbstractJedisPubSubHandler;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeJedisPubSubHandler extends AbstractJedisPubSubHandler {

    private final RedisConnection<Jedis> connection;
    private final ProxyServer server;
    private final Logger logger;

    public BungeeJedisPubSubHandler(RedisConnection<Jedis> connection, ProxyServer server, Logger logger) {
        this.connection = connection;
        this.server = server;
        this.logger = logger;
    }

    @Override
    protected RedisConnection<Jedis> connection() {
        return connection;
    }

    @Override
    protected void onRedisMessage(String channel, String message) {
        server.getPluginManager().callEvent(new JedisMessageEvent(channel, message));
    }

    @Override
    protected void onSubscriptionException(String channel, Exception ex) {
        logger.log(Level.WARNING, "Failed to subscribe to Redis channel " + channel, ex);
    }

    @Override
    protected void onShutdownTimeout() {
        logger.log(Level.SEVERE, "Pub-sub handler executor service did not terminate within timeout!");
    }
}
