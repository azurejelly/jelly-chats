package dev.azuuure.jellychats.connection.messaging.impl;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.connection.RedisConnection;
import dev.azuuure.jellychats.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.event.JedisMessageEvent;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VelocityRedisPubSubHandler implements RedisPubSubHandler {

    private final ProxyServer server;
    private final Logger logger;
    private final Map<String, JedisPubSub> subscriptions;
    private final ExecutorService executorService;
    private final RedisConnection<Jedis> connection;
    private volatile boolean running;

    @Inject
    public VelocityRedisPubSubHandler(ProxyServer server, Logger logger, RedisConnection<Jedis> connection) {
        this.server = server;
        this.logger = logger;
        this.subscriptions = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
        this.connection = connection;
        this.running = true;
    }

    @Override
    public boolean active() {
        return running;
    }

    @Override
    public void subscribe(String channel) {
        if (!running) {
            throw new RuntimeException("Pub-sub handler is inactive");
        }

        unsubscribe(channel);
        JedisPubSub pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channelName, String message) {
                if (!channel.equals(channelName)) {
                    return;
                }

                JedisMessageEvent event = new JedisMessageEvent(channelName, message);
                server.getEventManager().fireAndForget(event);
            }
        };

        subscriptions.put(channel, pubSub);
        executorService.submit(() -> {
            while (running) {
                try (Jedis jedis = connection.get()) {
                    jedis.subscribe(pubSub, channel);
                } catch (Exception ex) {
                    // i kinda hate this
                    logger.warn("Failed to subscribe to Redis channel '{}'", channel, ex);
                    subscriptions.remove(channel);
                }

                if (!running) {
                    break;
                }

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public void publish(String channel, String message) {
        if (!running) {
            throw new RuntimeException("Pub-sub handler is inactive");
        }

        try (Jedis jedis = connection.get()) {
            jedis.publish(channel, message);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to publish message", ex);
        }
    }

    @Override
    public void unsubscribe(String channel) {
        if (!running) {
            throw new RuntimeException("Pub-sub handler is inactive");
        }

        JedisPubSub pubSub = subscriptions.remove(channel);
        if (pubSub != null && pubSub.isSubscribed()) {
            pubSub.unsubscribe(channel);
        }
    }

    @Override
    public void shutdown() {
        if (!running) {
            throw new RuntimeException("Pub-sub handler is inactive");
        }

        subscriptions.forEach((ch, pubSub) -> {
            if (pubSub.isSubscribed()) {
                pubSub.unsubscribe(ch);
            }
        });

        running = false;
        subscriptions.clear();
        executorService.shutdown();
    }
} 