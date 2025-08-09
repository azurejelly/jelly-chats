package dev.azuuure.jellychats.core.chat.message;

import lombok.Builder;

/**
 * Represents a message sent to a private chat.
 *
 * @param author The author of the content.
 * @param content The content.
 * @since 1.2.0
 * @author azurejelly
 */
@Builder(builderClassName = "Builder")
public record PrivateChatMessage(
        String author,
        String prefix,
        String server,
        String suffix,
        String content
) {}