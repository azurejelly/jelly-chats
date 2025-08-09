package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.messenger.VelocityJedisChatMessenger;

public class MessengerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ChatMessenger.class)
                .to(VelocityJedisChatMessenger.class)
                .in(Scopes.SINGLETON);
    }
}
