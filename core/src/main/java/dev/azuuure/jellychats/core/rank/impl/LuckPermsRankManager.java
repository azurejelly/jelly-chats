package dev.azuuure.jellychats.core.rank.impl;

import dev.azuuure.jellychats.core.rank.RankManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

import java.util.UUID;

/**
 * A {@link RankManager} implementation that uses the <a href="https://luckperms.net/">LuckPerms</a>
 * plugin for rank prefixes and suffixes.
 *
 * @author azurejelly
 * @since 1.2.0
 * @see dev.azuuure.jellychats.core.rank.RankManager
 */
public class LuckPermsRankManager implements RankManager {

    private final LuckPerms luckPerms;

    public LuckPermsRankManager() {
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public String getPrefix(UUID uuid) {
        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) {
            return "";
        }

        CachedMetaData metadata = user.getCachedData().getMetaData();
        String prefix = metadata.getPrefix();

        if (prefix == null) {
            return "";
        }

        return prefix;
    }

    @Override
    public String getSuffix(UUID uuid) {
        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) {
            return "";
        }

        CachedMetaData metadata = user.getCachedData().getMetaData();
        String suffix = metadata.getSuffix();

        if (suffix == null) {
            return "";
        }

        return suffix;
    }
}
