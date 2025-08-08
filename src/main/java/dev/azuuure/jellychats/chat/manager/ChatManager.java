package dev.azuuure.jellychats.chat.manager;

import dev.azuuure.jellychats.chat.PrivateChat;

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
