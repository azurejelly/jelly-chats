package dev.azuuure.staffchat.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.azuuure.staffchat.chat.manager.ChatManager;
import dev.azuuure.staffchat.chat.manager.impl.DefaultChatManager;

public class ChatModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ChatManager.class).to(DefaultChatManager.class).in(Scopes.SINGLETON);
    }
}
