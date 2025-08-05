package dev.azuuure.jellychats.utils;

import lombok.experimental.UtilityClass;
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
}
