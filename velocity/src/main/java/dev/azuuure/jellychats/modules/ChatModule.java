package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.azuuure.jellychats.chat.VelocityChatManager;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;

public class ChatModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ChatManager.class)
                .to(VelocityChatManager.class)
                .in(Scopes.SINGLETON);
    }
}
