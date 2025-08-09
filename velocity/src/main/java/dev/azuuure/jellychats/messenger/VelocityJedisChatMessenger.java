package dev.azuuure.jellychats.messenger;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.connection.VelocityJedisPubSubHandler;
import dev.azuuure.jellychats.core.JellyChatsPlugin;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.core.messenger.impl.AbstractJedisChatMessenger;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

public class VelocityJedisChatMessenger extends AbstractJedisChatMessenger {

    @Inject private Gson gson;
    @Inject private ProxyServer server;
    @Inject private Logger logger;
    @Inject private JellyChatsPlugin plugin;

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
        return new VelocityJedisPubSubHandler(server, logger, connection);
    }
}
