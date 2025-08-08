package dev.azuuure.jellychats.connection.messaging;

public interface RedisPubSubHandler {

    void initialize();

    boolean active();

    void subscribe(String channel);

    void publish(String channel, String message);

    void unsubscribe(String channel);

    void shutdown();
} 