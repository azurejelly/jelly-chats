package dev.azuuure.jellychats.chat.message;

/**
 * Represents a message sent to a private chat.
 *
 * @param author The author of the content.
 * @param content The content.
 * @author azurejelly
 */
public record PrivateChatMessage(
        String author,
        String server,
        String content
) {}
