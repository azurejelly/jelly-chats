package dev.azuuure.jellychats.core.rank;

import java.util.UUID;

/**
 * Provides the prefixes and suffixes to be used in private chat messages.
 *
 * @author azurejelly
 * @since 1.2.0
 */
public interface RankManager {

    /**
     * Provides the rank prefix for a player based on its {@link UUID}.
     *
     * @param uuid The {@link UUID} of the player.
     * @return The prefix of the player.
     */
    String getPrefix(UUID uuid);

    /**
     * Provides the suffix for a player based on its {@link UUID}.
     *
     * @param uuid The {@link UUID} of the player.
     * @return The suffix of the player.
     */
    String getSuffix(UUID uuid);
}
