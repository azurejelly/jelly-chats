package dev.azuuure.staffchat.messenger;

import com.velocitypowered.api.command.CommandSource;
import dev.azuuure.staffchat.channel.PrivateChannel;

public interface PrivateChannelMessenger {

    void initialize();

    void send(CommandSource source, PrivateChannel channel, String message);

    void shutdown();
}
