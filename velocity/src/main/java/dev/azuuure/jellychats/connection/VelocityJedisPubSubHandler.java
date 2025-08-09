package dev.azuuure.jellychats.connection;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.messaging.AbstractJedisPubSubHandler;
import dev.azuuure.jellychats.event.JedisMessageEvent;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

public class VelocityJedisPubSubHandler extends AbstractJedisPubSubHandler {

    private final ProxyServer server;
    private final Logger logger;
    private final RedisConnection<Jedis> connection;

    public VelocityJedisPubSubHandler(ProxyServer server, Logger logger, RedisConnection<Jedis> connection) {
        this.server = server;
        this.logger = logger;
        this.connection = connection;
    }

    @Override
    protected RedisConnection<Jedis> connection() {
        return connection;
    }

    @Override
    protected void onJedisMessage(String channel, String message) {
        JedisMessageEvent event = new JedisMessageEvent(channel, message);
        server.getEventManager().fireAndForget(event);
    }

    @Override
    protected void onSubscriptionException(String channel, Exception ex) {
        logger.warn("Failed to subscribe to Redis channel {}", channel, ex);
    }

    @Override
    protected void onShutdownTimeout() {
        logger.warn("Pub-sub handler executor service did not terminate within timeout!");
    }
}