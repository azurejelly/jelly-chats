package dev.azuuure.jellychats.chat.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PrivateChatCommandData(String main, List<String> aliases) {}
