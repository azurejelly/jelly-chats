package dev.azuuure.staffchat.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.messenger.PrivateChannelMessenger;

public abstract class AbstractPrivateChannelCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            source.sendMessage(configuration().getComponent("commands.usage.staff"));
            return;
        }

        String message = String.join(" ", args);
        messenger().send(source, channel(), message);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(channel().getPermission());
    }

    protected abstract Configuration configuration();

    protected abstract PrivateChannelMessenger messenger();

    public abstract PrivateChannel channel();
}
