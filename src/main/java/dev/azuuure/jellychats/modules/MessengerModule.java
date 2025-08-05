package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.azuuure.jellychats.messenger.PrivateChatMessenger;
import dev.azuuure.jellychats.messenger.impl.RedisPrivateChatMessenger;

public class MessengerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(PrivateChatMessenger.class).to(RedisPrivateChatMessenger.class).in(Scopes.SINGLETON);
    }
}
