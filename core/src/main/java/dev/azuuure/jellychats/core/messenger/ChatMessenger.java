package dev.azuuure.jellychats.core.messenger;

import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.messenger.wrapper.MessageAuthor;

/**
 * Handles the process of sending messages to private chats.
 *
 * @author azurejelly
 * @since 1.2.0
 */
public interface ChatMessenger {

    /**
     * Initializes this {@link ChatMessenger} instance.
     */
    void initialize();

    /**
     * Reloads (i.e. shutdowns and re-initializes) this {@link ChatMessenger} instance.
     * The default implementation is usually good enough and is rarely (if never) changed.
     */
    default void reload() {
        this.shutdown();
        this.initialize();
    }

    /**
     * Sends a message to the specified {@link PrivateChat}.
     *
     * @param author The {@link MessageAuthor author} of the message.
     * @param chat The {@link PrivateChat chat} the message should be sent to.
     * @param message The message content.
     */
    void send(MessageAuthor author, PrivateChat chat, String message);

    /**
     * Shuts down this {@link ChatMessenger} instance.
     */
    void shutdown();
}
