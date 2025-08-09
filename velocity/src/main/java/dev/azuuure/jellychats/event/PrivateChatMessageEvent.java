package dev.azuuure.jellychats.event;

import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.chat.message.PrivateChatMessage;

public record PrivateChatMessageEvent(PrivateChat chat, PrivateChatMessage message) {}
