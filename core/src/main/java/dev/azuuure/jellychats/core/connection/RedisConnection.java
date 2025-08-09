package dev.azuuure.jellychats.core.connection;

public interface RedisConnection<T> {

    void connect();

    T get();

    void destroy();
}
