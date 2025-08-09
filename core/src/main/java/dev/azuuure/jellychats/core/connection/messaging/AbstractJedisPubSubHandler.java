package dev.azuuure.jellychats.core.connection.messaging;

import dev.azuuure.jellychats.core.connection.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Uses Jedis to receive and send messages through Redis.
 *
 * @author azurejelly
 * @since 1.2.0
 */
public abstract class AbstractJedisPubSubHandler implements RedisPubSubHandler {

    private Map<String, JedisPubSub> subscriptions;
    private ExecutorService executorService;
    private volatile boolean running = false;

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

                onRedisMessage(channelName, message);
            }
        };

        subscriptions.put(channel, pubSub);
        executorService.submit(() -> {
            while (running && pubSub.equals(subscriptions.get(channel))) {
                try (Jedis jedis = connection().get()) {
                    jedis.subscribe(pubSub, channel);
                } catch (Exception ex) {
                    onSubscriptionException(channel, ex);
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

        try (Jedis jedis = connection().get()) {
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
                onShutdownTimeout();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Should provide the {@link Jedis} based {@link RedisConnection}
     * to be used with this pub-sub handler.
     *
     * @return A {@link Jedis} based {@link RedisConnection}.
     */
    protected abstract RedisConnection<Jedis> connection();

    /**
     * Called when a message is received through Redis.
     *
     * @param channel The channel the message was received through.
     * @param message The content of the message as a {@link String}.
     */
    protected abstract void onRedisMessage(String channel, String message);

    /**
     * Called when an error occurs on a subscription thread.
     *
     * @param channel The channel the exception occurred in.
     * @param ex The exception that was caught.
     */
    protected abstract void onSubscriptionException(String channel, Exception ex);

    /**
     * Called when the {@link ExecutorService} in use takes too long to shut down
     * and is forcibly terminated.
     * <p>
     * This method only really exists because different platforms use different
     * logging methods.
     */
    protected abstract void onShutdownTimeout();
}