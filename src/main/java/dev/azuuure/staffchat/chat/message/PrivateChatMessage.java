package dev.azuuure.staffchat.chat.message;

/**
 * Represents a content sent to a private channel.
 *
 * @param author The author of the content.
 * @param content The content.
 * @author azurejelly
 */
public record PrivateChatMessage(
        String author,
        String content
) {}
