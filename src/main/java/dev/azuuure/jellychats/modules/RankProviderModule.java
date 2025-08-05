package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.rank.RankProvider;
import dev.azuuure.jellychats.rank.impl.DummyRankProvider;
import dev.azuuure.jellychats.rank.impl.LuckPermsRankProvider;
import org.slf4j.Logger;

public class RankProviderModule extends AbstractModule {

    @Provides
    @Singleton
    public RankProvider getRankProvider(ProxyServer server, Logger logger) {
        PluginManager pluginManager = server.getPluginManager();
        if (pluginManager.isLoaded("luckperms")) {
            logger.info("Will use LuckPerms to obtain player ranks");
            return new LuckPermsRankProvider();
        }

        logger.info("No compatible rank providers were found. Defaulting to a dummy implementation.");
        logger.info("No ranks will be shown in private chats.");
        return new DummyRankProvider();
    }
}
