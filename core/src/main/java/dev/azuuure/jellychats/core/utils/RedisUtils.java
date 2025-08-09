package dev.azuuure.jellychats.core.utils;

import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.impl.DefaultRedisConnection;
import lombok.experimental.UtilityClass;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;

/**
 * Contains some Redis-specific utilities.
 *
 * @author azurejelly
 * @since 1.2.0
 */
@UtilityClass
public class RedisUtils {

    /**
     * The default Redis timeout to be used.
     */
    public static final int DEFAULT_TIMEOUT = 5000;

    /**
     * Checks whether the provided {@link String} is a valid
     * Redis URI.
     *
     * @param str A {@link String}
     * @return true if the provided {@link String} matches the Redis URI syntax.
     */
    public static boolean isValidURI(String str) {
        try {
            URI uri = URI.create(str);
            return JedisURIHelper.isValid(uri);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Builds a {@link Jedis} {@link RedisConnection} by obtaining parameters
     * such as the URI, hostname and port from a {@link Configuration} instance.
     *
     * @param config The {@link Configuration} instance.
     * @return A {@link RedisConnection} instance that uses {@link Jedis} as its backend.
     */
    public static RedisConnection<Jedis> buildFrom(Configuration config) {
        ConfigurationNode parent = config.getRootNode().node("redis");
        String uri = parent.node("uri").getString();

        if (uri != null && !uri.isBlank()) {
            return new DefaultRedisConnection(uri);
        } else {
            return new DefaultRedisConnection(
                    parent.node("hostname").getString("127.0.0.1"),
                    parent.node("port").getInt(6379),
                    parent.node("username").getString(),
                    parent.node("password").getString()
            );
        }
    }
}
