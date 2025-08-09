package dev.azuuure.jellychats.core.messenger.impl;

import com.google.gson.Gson;
import dev.azuuure.jellychats.core.JellyChatsPlugin;
import dev.azuuure.jellychats.core.chat.PrivateChat;
import dev.azuuure.jellychats.core.chat.message.PrivateChatMessage;
import dev.azuuure.jellychats.core.connection.RedisConnection;
import dev.azuuure.jellychats.core.connection.messaging.RedisPubSubHandler;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.messenger.wrapper.MessageAuthor;
import dev.azuuure.jellychats.core.utils.RedisUtils;
import redis.clients.jedis.Jedis;

public abstract class AbstractJedisChatMessenger implements ChatMessenger {

    private RedisConnection<Jedis> connection;
    private RedisPubSubHandler pubSubHandler;

    @Override
    public void initialize() {
        this.connection = RedisUtils.buildFrom(plugin().getConfiguration());
        this.pubSubHandler = buildPubSubHandler(connection);
        this.connection.connect();
        this.pubSubHandler.initialize();
        this.plugin().getChatManager()
                .findAll()
                .stream()
                .map(PrivateChat::channel)
                .forEach(pubSubHandler::subscribe);
    }

    @Override
    public void send(MessageAuthor author, PrivateChat chat, String message) {
        PrivateChatMessage.Builder builder = PrivateChatMessage.builder()
                .server(author.server())
                .author(author.name())
                .content(message);

        if (!author.isConsole()) {
            builder.prefix(plugin().getRankManager().getPrefix(author.uuid()));
            builder.suffix(plugin().getRankManager().getSuffix(author.uuid()));
        }

        PrivateChatMessage content = builder.build();
        pubSubHandler.publish(chat.channel(), gson().toJson(content));
    }

    @Override
    public void shutdown() {
        if (pubSubHandler.active()) {
            pubSubHandler.shutdown();
        }

        connection.destroy();
    }

    protected abstract Gson gson();

    protected abstract JellyChatsPlugin plugin();

    protected abstract RedisPubSubHandler buildPubSubHandler(RedisConnection<Jedis> connection);
}
