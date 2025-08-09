package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.core.rank.RankManager;
import dev.azuuure.jellychats.core.rank.impl.DummyRankManager;
import dev.azuuure.jellychats.core.rank.impl.LuckPermsRankManager;
import org.slf4j.Logger;

public class RankProviderModule extends AbstractModule {

    @Provides
    @Singleton
    public RankManager getRankProvider(ProxyServer server, Logger logger) {
        PluginManager pluginManager = server.getPluginManager();
        if (pluginManager.isLoaded("luckperms")) {
            logger.info("Will use LuckPerms to obtain player ranks.");
            return new LuckPermsRankManager();
        }

        logger.warn("No compatible rank providers were found. Defaulting to a dummy implementation.");
        logger.warn("No ranks will be shown in private chats.");
        return new DummyRankManager();
    }
}
