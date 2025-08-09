package dev.azuuure.jellychats.core.chat.manager;

import dev.azuuure.jellychats.core.chat.PrivateChat;

import java.util.Collection;

public interface ChatManager {

    void initialize();

    void reload();

    void register(PrivateChat chat);

    PrivateChat find(String id);

    PrivateChat fromChannel(String channel);

    Collection<PrivateChat> findAll();

    void shutdown();
}
