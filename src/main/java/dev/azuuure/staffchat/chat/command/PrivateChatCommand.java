package dev.azuuure.staffchat.chat.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PrivateChatCommand(String main, List<String> aliases) {}
