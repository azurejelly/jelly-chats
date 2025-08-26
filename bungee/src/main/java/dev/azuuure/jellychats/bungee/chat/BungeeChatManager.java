package dev.azuuure.jellychats.bungee.chat;

import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.chat.command.PrivateChatCommandData;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeChatManager implements ChatManager {

    private final Map<String, PrivateChat> chats;
    private final Configuration config;
    private final Logger logger;

    public BungeeChatManager(Configuration config, Logger logger) {
        this.chats = new HashMap<>();
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void initialize() {
        ConfigurationNode base = config.getRootNode().node("chats");
        String channelPrefix = config.getString("default-channel-prefix", "jelly-chats/");

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
                logger.log(Level.WARNING, "Failed to load and register private chat identified by '" + k + "'", e);
            }
        });
    }

    @Override
    public void register(PrivateChat chat) {
//        SimpleCommand command = new PrivateChatCommand(configuration, messenger, chat);
//        CommandMeta.Builder metaBuilder = server.getCommandManager().metaBuilder(chat.command().main());
//        if (chat.command().aliases() != null && !chat.command().aliases().isEmpty()) {
//            metaBuilder.aliases(chat.command().aliases().toArray(new String[0]));
//        }
//
//        server.getCommandManager().register(metaBuilder.build(), command);
        chats.put(chat.id().toLowerCase(), chat);
        logger.info("Loaded chat with ID " + chat.id());
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
            logger.info("Unloaded chat with ID '" + id + "'");
//            server.getCommandManager().unregister(chat.command().main());
//            chat.command().aliases().forEach(alias -> server.getCommandManager().unregister(alias));
        });

        chats.clear();
    }
}
