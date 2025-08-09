package dev.azuuure.jellychats.core.connection;

/**
 * Handles a Redis connection.
 *
 * @author azurejelly
 * @param <T> The backend, for example Jedis.
 */
public interface RedisConnection<T> {

    void connect();

    T get();

    void destroy();
}
