package dev.azuuure.jellychats.core;

import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.rank.RankManager;

public interface JellyChatsPlugin {

    void init();

    void shutdown();

    Configuration getConfiguration();

    RankManager getRankManager();

    ChatManager getChatManager();

    ChatMessenger getChatMessenger();

    void setRankManager(RankManager rankManager);
}
