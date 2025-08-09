package dev.azuuure.jellychats.core.rank.impl;

import dev.azuuure.jellychats.core.rank.RankManager;

import java.util.UUID;

/**
 * Dummy rank provider, used when no rank plugins are detected.
 * Simply returns an empty string for prefixes and suffixes.
 *
 * @author azurejelly
 * @since 1.2.0
 * @see dev.azuuure.jellychats.core.rank.RankManager
 */
public class DummyRankManager implements RankManager {

    @Override
    public String getPrefix(UUID uuid) {
        return "";
    }

    @Override
    public String getSuffix(UUID uuid) {
        return "";
    }
}
