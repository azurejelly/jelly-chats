package dev.azuuure.jellychats.core.connection.messaging;

/**
 * Handles publishing messages to Redis channels
 * as well as receiving them.
 *
 * @author azurejelly
 * @since 1.2.0
 */
public interface RedisPubSubHandler {

    /**
     * Initializes the Redis pub-sub handler.
     */
    void initialize();

    /**
     * Whether the Redis pub-sub handler is active.
     *
     * @return true if active.
     */
    boolean active();

    /**
     * Subscribes to a Redis channel.
     *
     * @param channel The Redis channel to subscribe to.
     */
    void subscribe(String channel);

    /**
     * Publishes a message to a Redis channel.
     *
     * @param channel The Redis channel the message should be published to.
     * @param message The content of the message as a {@link String}.
     */
    void publish(String channel, String message);

    /**
     * Unsubscribes from a Redis channel.
     *
     * @param channel The channel to unsubscribe from.
     */
    void unsubscribe(String channel);

    /**
     * Shuts down the Redis pub-sub handler.
     */
    void shutdown();
} 