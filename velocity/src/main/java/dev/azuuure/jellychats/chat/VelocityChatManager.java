package dev.azuuure.jellychats.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.commands.AbstractPrivateChatCommand;
import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.chat.command.PrivateChatCommandData;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VelocityChatManager implements ChatManager {

    private final Configuration configuration;
    private final Logger logger;
    private final ProxyServer server;
    private final ChatMessenger messenger;
    private final Map<String, PrivateChat> chats;

    @Inject
    private VelocityChatManager(Configuration configuration, Logger logger, ProxyServer server, ChatMessenger messenger) {
        this.configuration = configuration;
        this.logger = logger;
        this.server = server;
        this.messenger = messenger;
        this.chats = new HashMap<>();
    }

    @Override
    public void initialize() {
        CommandManager commandManager = server.getCommandManager();
        ConfigurationNode base = configuration.getRootNode().node("chats");
        String channelPrefix = configuration.getString("default-channel-prefix", "jelly-chats/");

        base.childrenMap().forEach((k, v) -> {
            try {
                String id = k.toString();

                boolean enabled = v.node("enabled").getBoolean(false);
                String name = v.node("name").getString(id);
                String channel = v.node("channel").getString(channelPrefix + id);
                String permission = v.node("permission").getString("staff-chat.chat." + id);

                String commandName = v.node("command", "main").getString(id.toLowerCase() + "-chat");
                List<String> aliases = v.node("command", "aliases").getList(String.class);

                PrivateChat chat = PrivateChat.builder()
                        .id(id)
                        .name(name)
                        .channel(channel)
                        .enabled(enabled)
                        .permission(permission)
                        .command(
                                PrivateChatCommandData.builder()
                                        .main(commandName)
                                        .aliases(aliases)
                                        .build()
                        ).build();

                register(chat);
            } catch (SerializationException e) {
                logger.warn("Failed to load and register private chat identified by '{}'", k, e);
            }
        });
    }

    @Override
    public void register(PrivateChat chat) {
        SimpleCommand command = new AbstractPrivateChatCommand() {
            @Override
            protected Configuration configuration() {
                return configuration;
            }

            @Override
            protected ChatMessenger messenger() {
                return messenger;
            }

            @Override
            protected PrivateChat chat() {
                return chat;
            }
        };

        CommandMeta.Builder metaBuilder = server.getCommandManager().metaBuilder(chat.command().main());
        if (chat.command().aliases() != null && !chat.command().aliases().isEmpty()) {
            metaBuilder.aliases(chat.command().aliases().toArray(new String[0]));
        }

        server.getCommandManager().register(metaBuilder.build(), command);
        chats.put(chat.id().toLowerCase(), chat);
        logger.info("Loaded chat with ID {}", chat.id());
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
        chats.forEach((id,chat) -> {
            logger.info("Unloaded chat with ID '{}'", id);
            server.getCommandManager().unregister(chat.command().main());
            chat.command().aliases().forEach(alias -> server.getCommandManager().unregister(alias));
        });

        chats.clear();
    }
}
