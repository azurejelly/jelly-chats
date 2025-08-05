package dev.azuuure.jellychats.chat.message;

import lombok.Builder;

/**
 * Represents a message sent to a private chat.
 *
 * @param author The author of the content.
 * @param content The content.
 * @author azurejelly
 */
@Builder
public record PrivateChatMessage(
        String author,
        String prefix,
        String server,
        String suffix,
        String content
) {}
