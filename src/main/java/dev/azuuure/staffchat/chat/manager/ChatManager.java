package dev.azuuure.staffchat.chat.manager;

import dev.azuuure.staffchat.chat.PrivateChat;
import dev.azuuure.staffchat.configuration.Configuration;

import java.util.Collection;

public interface ChatManager {

    void initialize();

    void register(PrivateChat chat);

    PrivateChat find(String id);

    PrivateChat fromChannel(String channel);

    Collection<PrivateChat> findAll();

    void shutdown();
}
