package dev.azuuure.jellychats.core;

import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.rank.RankManager;

/**
 * The main JellyChats plugin class.
 *
 * @author azurejelly
 * @since 1.2.0
 */
public interface JellyChatsPlugin {

    /**
     * Initializes the plugin.
     */
    void init();

    /**
     * Shuts down the plugin.
     */
    void shutdown();

    /**
     * Provides access to the main configuration file.
     * @return An instance of {@link Configuration}.
     */
    Configuration getConfiguration();

    /**
     * Provides access to the {@link RankManager} in use,
     * which handles prefixes and suffixes in private chat
     * messages.
     *
     * @return An instance of {@link RankManager}.
     */
    RankManager getRankManager();

    /**
     * Provides access to the {@link ChatManager} in use,
     * which acts as a registry for all private chats.
     *
     * @return An instance of {@link ChatManager}.
     */
    ChatManager getChatManager();

    /**
     * Provides access to the {@link ChatMessenger} in use,
     * which is used to send messages to private chats.
     *
     * @return An instance of {@link ChatMessenger}.
     */
    ChatMessenger getChatMessenger();

    /**
     * Overrides the default {@link RankManager} with another
     * implementation.
     *
     * @param rankManager A {@link RankManager} implementation.
     */
    void setRankManager(RankManager rankManager);
}
