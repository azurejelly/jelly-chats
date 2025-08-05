package dev.azuuure.jellychats.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.azuuure.jellychats.chat.PrivateChat;
import dev.azuuure.jellychats.configuration.Configuration;
import dev.azuuure.jellychats.messenger.PrivateChatMessenger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public abstract class AbstractPrivateChatCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!chat().enabled()) {
            source.sendMessage(configuration().getComponent("messages.chat-disabled"));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(
                    configuration().getComponent("messages.usage",
                            Placeholder.unparsed("name", chat().command().main())
                    )
            );
        } else {
            String message = String.join(" ", args);
            messenger().send(source, chat(), message);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(chat().permission());
    }

    protected abstract Configuration configuration();

    protected abstract PrivateChatMessenger messenger();

    protected abstract PrivateChat chat();
}
