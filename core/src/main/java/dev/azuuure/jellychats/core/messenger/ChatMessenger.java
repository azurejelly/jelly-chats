package dev.azuuure.jellychats.core.messenger;

import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.messenger.wrapper.MessageAuthor;

public interface ChatMessenger {

    void initialize();

    void reload();

    void send(MessageAuthor author, PrivateChat chat, String message);

    void shutdown();
}
