package dev.azuuure.jellychats.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.messenger.wrapper.MessageAuthor;
import dev.azuuure.jellychats.core.utils.ChatFormatUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;

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
                            Placeholder.unparsed("usage", chat().command().main() + " <message>")
                    )
            );
        } else {
            MessageAuthor author;

            if (source instanceof Player player) {
                UUID uuid = player.getUniqueId();
                String name = player.getUsername();
                String server = player.getCurrentServer()
                        .map(connection -> connection.getServerInfo().getName())
                        .orElse(configuration().getString("unknown-server", ChatFormatUtil.DEFAULT_SERVER));

                author = MessageAuthor.from(uuid, name, server);
            } else {
                author = MessageAuthor.asConsole(
                        configuration().getString("console-name", ChatFormatUtil.DEFAULT_CONSOLE_NAME),
                        configuration().getString("unknown-server", ChatFormatUtil.DEFAULT_SERVER)
                );
            }

            String message = String.join(" ", args);
            messenger().send(author, chat(), message);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(chat().permission());
    }

    protected abstract Configuration configuration();

    protected abstract ChatMessenger messenger();

    protected abstract PrivateChat chat();
}
