package dev.azuuure.jellychats.core.chat.manager;

import dev.azuuure.jellychats.core.chat.PrivateChat;

import java.util.Collection;

/**
 * Acts as a registry which loads, holds and unloads
 * each available {@link PrivateChat}.
 *
 * @since 1.2.0
 * @author azurejelly
 */
public interface ChatManager {

    /**
     * Initializes the {@link ChatManager}.
     */
    void initialize();

    /**
     * Reloads (i.e. shutdowns and re-initializes) this {@link ChatManager} instance.
     * The default implementation is usually good enough and is rarely (if never) changed.
     */
    default void reload() {
        this.shutdown();
        this.initialize();
    }

    /**
     * Registers a new {@link PrivateChat}.
     *
     * @param chat The {@link PrivateChat} to be registered.
     */
    void register(PrivateChat chat);

    /**
     * Finds a {@link PrivateChat} by its ID.
     *
     * @param id The ID of the {@link PrivateChat}.
     * @return A {@link PrivateChat} or null if no such chat exists.
     */
    PrivateChat find(String id);

    /**
     * Finds a {@link PrivateChat} by the channel it is using.
     *
     * @param channel The channel of the {@link PrivateChat}.
     * @return A {@link PrivateChat} or null if no such chat exists.
     */
    PrivateChat fromChannel(String channel);

    /**
     * Finds all registered {@link PrivateChat private chats}.
     * @return A {@link Collection} with each loaded {@link PrivateChat}.
     */
    Collection<PrivateChat> findAll();

    /**
     * Shuts down this {@link ChatManager} instance.
     */
    void shutdown();
}
