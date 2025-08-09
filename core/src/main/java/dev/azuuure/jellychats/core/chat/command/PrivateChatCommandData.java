package dev.azuuure.jellychats.core.chat.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PrivateChatCommandData(String main, List<String> aliases) {}
