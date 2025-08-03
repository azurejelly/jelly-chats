package dev.azuuure.staffchat.channel.message;

/**
 * Represents a content sent to a private channel.
 *
 * @param author The author of the content.
 * @param content The content.
 * @author azurejelly
 */
public record PrivateChannelMessage(
        String author,
        String content
) {}
