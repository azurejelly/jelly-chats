package dev.azuuure.staffchat;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.staffchat.commands.AdminChatCommand;
import dev.azuuure.staffchat.commands.StaffChatCommand;
import dev.azuuure.staffchat.configuration.Configuration;
import dev.azuuure.staffchat.listener.JedisMessageListener;
import dev.azuuure.staffchat.listener.PrivateChannelMessageListener;
import dev.azuuure.staffchat.messenger.PrivateChannelMessenger;
import dev.azuuure.staffchat.messenger.impl.RedisPrivateChannelMessenger;
import dev.azuuure.staffchat.modules.ConfigurationModule;
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
    private Configuration configuration;
    private PrivateChannelMessenger messenger;

    @Inject
    public StaffChat(ProxyServer server, Logger logger, Injector injector) {
        this.server = server;
        this.logger = logger;
        this.injector = injector.createChildInjector(new ConfigurationModule());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        long start = System.currentTimeMillis();

        configuration = injector.getInstance(Configuration.class);

        messenger = injector.getInstance(RedisPrivateChannelMessenger.class);
        messenger.initialize();

        server.getEventManager().register(this, injector.getInstance(PrivateChannelMessageListener.class));
        server.getEventManager().register(this, injector.getInstance(JedisMessageListener.class));

        server.getCommandManager().register(
                server.getCommandManager()
                        .metaBuilder("staff-chat")
                        .aliases("staffchat", "sc", "s")
                        .plugin(this)
                        .build(),
                new StaffChatCommand(messenger, configuration)
        );

        server.getCommandManager().register(
                server.getCommandManager()
                        .metaBuilder("admin-chat")
                        .aliases("adminchat", "ac", "a")
                        .plugin(this)
                        .build(),
                new AdminChatCommand(messenger, configuration)
        );

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin initialization took {} ms", (end - start));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        long start = System.currentTimeMillis();
        messenger.shutdown();

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin shutdown took {} ms", (end - start));
        }
    }
}
