package dev.azuuure.staffchat.commands;

import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.messenger.PrivateChannelMessenger;

public class StaffChatCommand extends AbstractPrivateChannelCommand {

    private final PrivateChannelMessenger messenger;
    private final Configuration config;

    public StaffChatCommand(PrivateChannelMessenger messenger, Configuration config) {
        this.messenger = messenger;
        this.config = config;
    }

    @Override
    protected Configuration configuration() {
        return config;
    }

    @Override
    protected PrivateChannelMessenger messenger() {
        return messenger;
    }

    @Override
    public PrivateChannel channel() {
        return PrivateChannel.STAFF;
    }
} 