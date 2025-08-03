package dev.azuuure.staffchat.messenger.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.channel.message.PrivateChannelMessage;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.connection.RedisConnection;
import dev.azuuure.staffchat.connection.impl.DefaultRedisConnection;
import dev.azuuure.staffchat.connection.messaging.RedisPubSubHandler;
import dev.azuuure.staffchat.connection.messaging.impl.VelocityRedisPubSubHandler;
import dev.azuuure.staffchat.messenger.PrivateChannelMessenger;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class RedisPrivateChannelMessenger implements PrivateChannelMessenger {

    private final Gson gson;
    private final Configuration config;
    private final RedisConnection<Jedis> connection;
    private final RedisPubSubHandler pubSubHandler;

    @Inject
    public RedisPrivateChannelMessenger(Gson gson, Configuration config, ProxyServer server, Logger logger) {
        this.gson = gson;
        this.config = config;
        this.connection = new DefaultRedisConnection(config.getString("redis.uri", "redis://localhost:6379"));
        this.pubSubHandler = new VelocityRedisPubSubHandler(server, logger, connection);
    }

    @Override
    public void initialize() {
        connection.connect();

        Arrays.stream(PrivateChannel.values())
                .forEach((ch) -> pubSubHandler.subscribe(ch.getChannel()));
    }

    @Override
    public void send(CommandSource source, PrivateChannel channel, String message) {
        String author = source instanceof Player player
                ? player.getUsername()
                : config.getString("console-name", "Console");

        PrivateChannelMessage content = new PrivateChannelMessage(author, message);
        pubSubHandler.publish(channel.getChannel(), gson.toJson(content));
    }

    @Override
    public void shutdown() {
        if (pubSubHandler.active()) {
            pubSubHandler.shutdown();
        }

        connection.destroy();
    }
}
