package dev.azuuure.staffchat;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.chat.manager.ChatManager;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.listener.JedisMessageListener;
import dev.azuuure.staffchat.listener.PrivateChatMessageListener;
import dev.azuuure.staffchat.messenger.PrivateChatMessenger;
import dev.azuuure.staffchat.modules.ChatModule;
import dev.azuuure.staffchat.modules.ConfigurationModule;
import dev.azuuure.staffchat.modules.MessengerModule;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "staff-chat",
        name = "staff-chat",
        description = "Redis-based staff chat for Velocity proxies",
        authors = { "azurejelly" },
        version = BuildConstants.VERSION
)
public class StaffChat {

    private final ProxyServer server;
    private final Logger logger;
    private final Injector injector;

    @Getter
    private Configuration configuration;

    @Getter
    private PrivateChatMessenger messenger;

    @Getter
    private ChatManager chatManager;

    @Inject
    public StaffChat(ProxyServer server, Logger logger, Injector injector) {
        this.server = server;
        this.logger = logger;
        this.injector = injector.createChildInjector(
                new ConfigurationModule(),
                new MessengerModule(),
                new ChatModule()
        );
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        long start = System.currentTimeMillis();

        configuration = injector.getInstance(Configuration.class);
        chatManager = injector.getInstance(ChatManager.class);
        chatManager.initialize();

        messenger = injector.getInstance(PrivateChatMessenger.class);
        messenger.initialize();

        server.getEventManager().register(this, injector.getInstance(PrivateChatMessageListener.class));
        server.getEventManager().register(this, injector.getInstance(JedisMessageListener.class));

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin initialization took {} ms", (end - start));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        long start = System.currentTimeMillis();
        chatManager.shutdown();
        messenger.shutdown();

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin shutdown took {} ms", (end - start));
        }
    }
}
