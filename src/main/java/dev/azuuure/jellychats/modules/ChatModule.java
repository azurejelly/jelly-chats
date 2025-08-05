package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.azuuure.jellychats.chat.manager.ChatManager;
import dev.azuuure.jellychats.chat.manager.impl.DefaultChatManager;

public class ChatModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ChatManager.class).to(DefaultChatManager.class).in(Scopes.SINGLETON);
    }
}
