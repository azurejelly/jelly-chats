package dev.azuuure.jellychats.chat;

import dev.azuuure.jellychats.chat.command.PrivateChatCommandData;
import lombok.Builder;

@Builder
public record PrivateChat(
        String id,
        boolean enabled,
        String channel,
        String name,
        String permission,
        PrivateChatCommandData command
) {}
