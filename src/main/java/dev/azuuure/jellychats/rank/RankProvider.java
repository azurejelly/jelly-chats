package dev.azuuure.jellychats.rank;

import com.velocitypowered.api.proxy.Player;

public interface RankProvider {

    String getPrefix(Player player);

    String getSuffix(Player player);
}
