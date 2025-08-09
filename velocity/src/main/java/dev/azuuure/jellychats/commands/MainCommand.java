package dev.azuuure.jellychats.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.azuuure.jellychats.core.BuildConstants;
import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;

// FIXME: should probably use a command manager
public class MainCommand implements SimpleCommand {

    @Inject private Configuration config;
    @Inject private Logger logger;
    @Inject private ChatManager chatManager;
    @Inject private ChatMessenger messenger;

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (args.length == 0) {
            source.sendMessage(
                    config.getComponent("messages.main.no-args",
                            Placeholder.unparsed("version", BuildConstants.VERSION)
                    )
            );
        } else {
            String action = args[0].toLowerCase();
            switch (action) {
                case "reload" -> reload(source);
                case "toggle" -> toggle(source, args);
                case "help" -> help(source);
                default -> unknown(source);
            }
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("jelly-chats.admin");
    }

    public void unknown(CommandSource source) {
        source.sendMessage(config.getComponent("messages.main.unknown-args"));
    }

    public void help(CommandSource source) {
        source.sendMessage(config.getComponent("messages.main.help"));
    }

    public void toggle(CommandSource source, String[] args) {
        if (args.length < 2) {
            source.sendMessage(
                    config.getComponent("messages.main.usage",
                            Placeholder.unparsed("usage", "/jelly-chat toggle <id>")
                    )
            );
        } else {
            String id = args[1].toLowerCase();
            PrivateChat chat = chatManager.find(id);
            boolean previous = chat.enabled();

            chat.enabled(!previous);
            config.set("chats." + chat.id() + ".enabled", !previous);

            try {
                config.save();
            } catch (ConfigurateException e) {
                logger.error("Failed to save configuration", e);
            } finally {
                source.sendMessage(
                        config.getComponent("messages.main.toggle." + (previous ? "disabled" : "enabled"),
                                Placeholder.unparsed("id", id),
                                Placeholder.unparsed("name", chat.name())
                        )
                );
            }
        }
    }

    public void reload(CommandSource source) {
        long start = System.currentTimeMillis();
        logger.info("Reloading jelly-chats...");

        try {
            logger.info("Re-initializing configuration...");
            config.reload();
        } catch (ConfigurateException ex) {
            fail(source, start);
            logger.error("Failed to reload config files", ex);
            return;
        }

        try {
            logger.info("Re-initializing chat manager...");
            chatManager.reload();
        } catch (RuntimeException ex) {
            fail(source, start);
            logger.error("Failed to reload chat manager", ex);
            return;
        }

        try {
            logger.info("Re-initializing messenger...");
            messenger.reload();
        } catch (RuntimeException ex) {
            fail(source, start);
            logger.error("Failed to reload messenger", ex);
            return;
        }

        long end = System.currentTimeMillis();
        logger.info("Done! Plugin reload took {} ms.", end - start);
        source.sendMessage(
                config.getComponent("messages.main.reload.success",
                        Placeholder.unparsed("time", String.valueOf(end - start))
                )
        );
    }

    private void fail(CommandSource source, long start) {
        long end = System.currentTimeMillis();
        source.sendMessage(
                config.getComponent("messages.main.reload.fail",
                        Placeholder.unparsed("time", String.valueOf(end - start))
                )
        );
    }
}
