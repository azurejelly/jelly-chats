package dev.azuuure.jellychats.connection.messaging.impl;

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
import java.util.concurrent.TimeUnit;

public class VelocityRedisPubSubHandler implements RedisPubSubHandler {

    private final ProxyServer server;
    private final Logger logger;
    private final RedisConnection<Jedis> connection;

    private Map<String, JedisPubSub> subscriptions;
    private ExecutorService executorService;
    private volatile boolean running;

    public VelocityRedisPubSubHandler(ProxyServer server, Logger logger, RedisConnection<Jedis> connection) {
        this.server = server;
        this.logger = logger;
        this.connection = connection;
        this.running = false;
    }

    @Override
    public void initialize() {
        if (running) {
            throw new IllegalStateException("Attempted to initialize a pub-sub handler that is already active");
        }

        this.executorService = Executors.newCachedThreadPool();
        this.subscriptions = new ConcurrentHashMap<>();
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
            while (running && pubSub.equals(subscriptions.get(channel))) {
                try (Jedis jedis = connection.get()) {
                    jedis.subscribe(pubSub, channel);
                } catch (Exception ex) {
                    // i kinda hate this
                    logger.warn("Failed to subscribe to Redis channel '{}'", channel, ex);
                    subscriptions.remove(channel);
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

        this.subscriptions.values().forEach(JedisPubSub::unsubscribe);
        this.running = false;
        this.subscriptions = null;
        this.executorService.shutdownNow();

        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Pub-sub handler executor service did not terminate within timeout!");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
} 