package dev.azuuure.jellychats.core.rank.impl;

import dev.azuuure.jellychats.core.rank.RankManager;

import java.util.UUID;

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
