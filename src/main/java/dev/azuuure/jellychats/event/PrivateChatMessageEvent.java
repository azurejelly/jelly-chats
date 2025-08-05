package dev.azuuure.jellychats.event;

import dev.azuuure.jellychats.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.chat.PrivateChat;

public record PrivateChatMessageEvent(PrivateChat chat, PrivateChatMessage message) {}
