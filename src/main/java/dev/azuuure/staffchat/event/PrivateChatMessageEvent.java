package dev.azuuure.staffchat.event;

import dev.azuuure.staffchat.chat.message.PrivateChatMessage;
import dev.azuuure.staffchat.chat.PrivateChat;

public record PrivateChatMessageEvent(PrivateChat chat, PrivateChatMessage message) {}
