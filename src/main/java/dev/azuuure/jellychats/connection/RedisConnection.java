package dev.azuuure.jellychats.connection;

public interface RedisConnection<T> {

    void connect();

    T get();

    void destroy();
}
