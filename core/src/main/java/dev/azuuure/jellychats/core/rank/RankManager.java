package dev.azuuure.jellychats.core.rank;

import java.util.UUID;

public interface RankManager {

    String getPrefix(UUID uuid);

    String getSuffix(UUID uuid);
}
