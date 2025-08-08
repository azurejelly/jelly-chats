package dev.azuuure.jellychats.utils;

import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.connection.RedisConnection;
import dev.azuuure.jellychats.connection.impl.DefaultRedisConnection;
import lombok.experimental.UtilityClass;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;

@UtilityClass
public class RedisUtils {

    public static final int DEFAULT_TIMEOUT = 5000;

    public static boolean isValidURI(String str) {
        try {
            URI uri = URI.create(str);
            return JedisURIHelper.isValid(uri);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

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
