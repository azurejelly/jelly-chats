package dev.azuuure.staffchat.chat;

import dev.azuuure.staffchat.chat.command.PrivateChatCommand;
import lombok.Builder;

@Builder
public record PrivateChat(String id, boolean enabled, String channel, String name, String permission, PrivateChatCommand command) {}
