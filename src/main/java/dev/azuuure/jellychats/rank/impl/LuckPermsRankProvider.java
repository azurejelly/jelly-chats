package dev.azuuure.jellychats.rank.impl;

import com.velocitypowered.api.proxy.Player;
import dev.azuuure.jellychats.rank.RankProvider;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class LuckPermsRankProvider implements RankProvider {

    private final LuckPerms luckPerms;

    public LuckPermsRankProvider() {
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
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
    public String getSuffix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
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
