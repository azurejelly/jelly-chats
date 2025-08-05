package dev.azuuure.staffchat.chat.manager.impl;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.chat.PrivateChat;
import dev.azuuure.staffchat.chat.command.PrivateChatCommand;
import dev.azuuure.staffchat.chat.manager.ChatManager;
import dev.azuuure.staffchat.commands.AbstractPrivateChatCommand;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.messenger.PrivateChatMessenger;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultChatManager implements ChatManager {

    private final Configuration configuration;
    private final Logger logger;
    private final ProxyServer server;
    private final PrivateChatMessenger messenger;
    private final Map<String, PrivateChat> chats;

    @Inject
    private DefaultChatManager(Configuration configuration, Logger logger, ProxyServer server, PrivateChatMessenger messenger) {
        this.configuration = configuration;
        this.logger = logger;
        this.server = server;
        this.messenger = messenger;
        this.chats = new HashMap<>();
    }

    @Override
    public void initialize() {
        CommandManager commandManager = server.getCommandManager();
        ConfigurationNode base = configuration.getConfigurationNode().node("chats");

        base.childrenMap().forEach((k, v) -> {
            try {
                String id = k.toString();
                logger.info("Loading private chat with ID '{}'", id);

                boolean enabled = v.node("enabled").getBoolean(true);
                String name = v.node("name").getString(id);
                String channel = v.node("channel").getString("staff-chat/" + id);
                String permission = v.node("permission").getString("staff-chat.chat." + id);

                ConfigurationNode commandNode = v.node("command");
                String commandName = commandNode.node("main").getString(id.toLowerCase() + "-chat");
                List<String> aliases = commandNode.node("aliases").getList(String.class);

                PrivateChat chat = PrivateChat.builder()
                        .id(id)
                        .name(name)
                        .channel(channel)
                        .enabled(enabled)
                        .permission(permission)
                        .command(
                                PrivateChatCommand.builder()
                                        .main(commandName)
                                        .aliases(aliases)
                                        .build()
                        ).build();

                SimpleCommand command = new AbstractPrivateChatCommand() {
                    @Override
                    protected Configuration configuration() {
                        return configuration;
                    }

                    @Override
                    protected PrivateChatMessenger messenger() {
                        return messenger;
                    }

                    @Override
                    protected PrivateChat chat() {
                        return chat;
                    }
                };

                CommandMeta.Builder metaBuilder = commandManager.metaBuilder(chat.command().main());
                if (aliases != null && !aliases.isEmpty()) {
                    metaBuilder.aliases(aliases.toArray(new String[0]));
                }

                commandManager.register(metaBuilder.build(), command);
                chats.put(id, chat);
            } catch (SerializationException e) {
                logger.warn("Failed to load and register private chat identified by '{}'", k, e);
            }
        });
    }

    @Override
    public void register(PrivateChat chat) {
        chats.put(chat.id(), chat);
    }

    @Override
    public PrivateChat find(String id) {
        return chats.get(id);
    }

    @Override
    public PrivateChat fromChannel(String channel) {
        return chats.values()
                .stream()
                .filter(chat -> chat.channel().equals(channel))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<PrivateChat> findAll() {
        return chats.values()
                .stream()
                .toList();
    }

    @Override
    public void shutdown() {
        chats.values().forEach((chat) -> server.getCommandManager().unregister(chat.command().main()));
        chats.clear();
    }
}
