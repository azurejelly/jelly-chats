package dev.azuuure.jellychats.core.connection.impl;

import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.utils.RedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class DefaultRedisConnection implements RedisConnection<Jedis> {

    private final String hostname;
    private final int port;
    private final String username;
    private final String password;
    private final String uri;
    private JedisPool pool;

    public DefaultRedisConnection(String uri) {
        if (!RedisUtils.isValidURI(uri)) {
            throw new IllegalArgumentException(
                    "The Redis URI is invalid. For help with the syntax, check "
                            + "https://redis.github.io/lettuce/user-guide/connecting-redis/#uri-syntax"
            );
        }

        this.hostname = null;
        this.port = 6379;
        this.username = null;
        this.password = null;
        this.uri = uri;
    }

    public DefaultRedisConnection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.username = null;
        this.password = null;
        this.uri = null;
    }

    public DefaultRedisConnection(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.uri = null;
    }

    public DefaultRedisConnection(String hostname, int port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = null;
        this.password = password;
        this.uri = null;
    }

    @Override
    public void connect() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinEvictableIdleDuration(Duration.ofMillis(30000));
        config.setTimeBetweenEvictionRuns(Duration.ofMillis(15000));
        config.setTestWhileIdle(true);

        boolean hasUsername = username != null && !username.isBlank();
        boolean hasPassword = password != null && !password.isBlank();

        if (uri != null) {
            this.pool = new JedisPool(config, uri);
        } else if (hasUsername && hasPassword) {
            this.pool = new JedisPool(config, hostname, port, RedisUtils.DEFAULT_TIMEOUT, username, password);
        } else if (hasPassword) {
            this.pool = new JedisPool(config, hostname, port, RedisUtils.DEFAULT_TIMEOUT, password);
        } else {
            this.pool = new JedisPool(config, hostname, port);
        }
    }

    @Override
    public Jedis get() {
        if (pool == null) {
            throw new IllegalStateException("The Redis connection has not been initialized yet!");
        }

        return pool.getResource();
    }

    @Override
    public void destroy() {
        if (pool == null) {
            throw new IllegalStateException("The Redis connection has not been initialized yet!");
        }

        pool.close();
        pool.destroy();
    }
}
