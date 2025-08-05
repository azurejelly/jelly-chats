package dev.azuuure.staffchat.messenger;

import com.velocitypowered.api.command.CommandSource;
import dev.azuuure.staffchat.chat.PrivateChat;

public interface PrivateChatMessenger {

    void initialize();

    void send(CommandSource source, PrivateChat chat, String message);

    void shutdown();
}
