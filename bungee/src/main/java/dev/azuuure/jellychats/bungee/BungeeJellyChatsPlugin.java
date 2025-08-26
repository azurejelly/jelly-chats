package dev.azuuure.jellychats.bungee;

import dev.azuuure.jellychats.bungee.chat.messenger.BungeeJedisChatMessenger;
import dev.azuuure.jellychats.core.JellyChatsPlugin;
import dev.azuuure.jellychats.core.JellyChatsPluginProvider;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.rank.RankManager;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class BungeeJellyChatsPlugin extends Plugin implements JellyChatsPlugin {

    private static BungeeJellyChatsPlugin instance;

    private Configuration config;
    private RankManager rankManager;
    private ChatManager chatManager;
    private ChatMessenger chatMessenger;

    @Override
    public void onLoad() {
        instance = this;
        JellyChatsPluginProvider.setInstance(this);
    }

    @Override
    public void onEnable() {
        try {
            this.init();
        } catch (RuntimeException ex) {
            this.getLogger().log(Level.SEVERE, "Failed to initialize JellyChats", ex);
        }
    }

    @Override
    public void onDisable() {
        try {
            this.shutdown();
        } catch (RuntimeException ex) {
            this.getLogger().log(Level.SEVERE, "Failed to gracefully shutdown JellyChats", ex);
        }
    }

    @Override
    @SneakyThrows
    public void init() {
        this.config = new Configuration(this.getDataFolder().toPath(), "config.yml");
        this.chatMessenger = new BungeeJedisChatMessenger(this, getProxy(), getLogger());
    }

    @Override
    public void shutdown() {
        if (chatManager != null) {
            chatManager.shutdown();
        }

        if (chatMessenger != null) {
            chatMessenger.shutdown();
        }
    }

    @Override
    public Configuration getConfiguration() {
        return config;
    }

    @Override
    public RankManager getRankManager() {
        return rankManager;
    }

    @Override
    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public ChatMessenger getChatMessenger() {
        return chatMessenger;
    }

    @Override
    public void setRankManager(RankManager rankManager) {
        this.rankManager = rankManager;
        this.getLogger().info(
                "The previously selected rank manager has been overridden with "
                        + rankManager.getClass().getName()
        );
    }
}
