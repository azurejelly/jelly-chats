package dev.azuuure.jellychats.core.chat;

import dev.azuuure.jellychats.core.chat.command.PrivateChatCommandData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrivateChat {

    private final String id;
    private boolean enabled;
    private String channel;
    private String name;
    private String permission;
    private PrivateChatCommandData command;
}
