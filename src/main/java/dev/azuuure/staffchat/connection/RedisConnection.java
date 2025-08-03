package dev.azuuure.staffchat.connection;

public interface RedisConnection<T> {

    void connect();

    T get();

    void destroy();
}
