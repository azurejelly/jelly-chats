package dev.azuuure.jellychats.rank.impl;

import com.velocitypowered.api.proxy.Player;
import dev.azuuure.jellychats.rank.RankProvider;

public class DummyRankProvider implements RankProvider {

    @Override
    public String getPrefix(Player player) {
        return "";
    }

    @Override
    public String getSuffix(Player player) {
        return "";
    }
}
