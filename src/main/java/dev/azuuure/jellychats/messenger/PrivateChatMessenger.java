package dev.azuuure.jellychats.messenger;

import com.velocitypowered.api.command.CommandSource;
import dev.azuuure.jellychats.chat.PrivateChat;

public interface PrivateChatMessenger {

    void initialize();

    void send(CommandSource source, PrivateChat chat, String message);

    void shutdown();
}
