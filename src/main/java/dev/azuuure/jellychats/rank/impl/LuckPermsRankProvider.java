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
        return metadata.getPrefix();
    }

    @Override
    public String getSuffix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }

        CachedMetaData metadata = user.getCachedData().getMetaData();
        return metadata.getSuffix();
    }
}
