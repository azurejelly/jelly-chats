package dev.azuuure.jellychats;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.azuuure.jellychats.commands.MainCommand;
import dev.azuuure.jellychats.core.BuildConstants;
import dev.azuuure.jellychats.core.JellyChatsPlugin;
import dev.azuuure.jellychats.core.JellyChatsPluginProvider;
import dev.azuuure.jellychats.core.chat.manager.ChatManager;
import dev.azuuure.jellychats.core.configuration.Configuration;
import dev.azuuure.jellychats.core.messenger.ChatMessenger;
import dev.azuuure.jellychats.core.rank.RankManager;
import dev.azuuure.jellychats.listener.JedisMessageListener;
import dev.azuuure.jellychats.listener.PrivateChatMessageListener;
import dev.azuuure.jellychats.modules.*;
import org.slf4j.Logger;

@Plugin(
        id = "jelly-chats",
        name = "JellyChats",
        description = "Highly customizable Redis-based private chats for Velocity proxies",
        authors = { "azurejelly" },
        version = BuildConstants.VERSION,
        dependencies = {
                @Dependency(id = "luckperms", optional = true)
        }
)
public class VelocityJellyChatsPlugin implements JellyChatsPlugin {

    private static VelocityJellyChatsPlugin instance;

    private final ProxyServer server;
    private final Logger logger;
    private final Injector injector;

    private RankManager rankManager;
    private Configuration configuration;
    private ChatMessenger messenger;
    private ChatManager chatManager;

    @Inject
    public VelocityJellyChatsPlugin(ProxyServer server, Logger logger, Injector injector) {
        JellyChatsPluginProvider.setInstance(this);
        instance = this;

        this.server = server;
        this.logger = logger;
        this.injector = injector.createChildInjector(
                new PluginModule(this),
                new RankProviderModule(),
                new ConfigurationModule(),
                new MessengerModule(),
                new ChatModule()
        );
    }

    @Override
    public void init() {
        rankManager = injector.getInstance(RankManager.class);
        configuration = injector.getInstance(Configuration.class);

        chatManager = injector.getInstance(ChatManager.class);
        chatManager.initialize();

        messenger = injector.getInstance(ChatMessenger.class);
        messenger.initialize();

        server.getEventManager().register(this, injector.getInstance(PrivateChatMessageListener.class));
        server.getEventManager().register(this, injector.getInstance(JedisMessageListener.class));

        server.getCommandManager().register(
                server.getCommandManager().metaBuilder("jelly-chats")
                        .aliases("chats", "private-chats", "privatechats")
                        .plugin(this)
                        .build(),
                injector.getInstance(MainCommand.class)
        );
    }

    @Override
    public void shutdown() {
        chatManager.shutdown();
        messenger.shutdown();
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public RankManager getRankManager() {
        return rankManager;
    }

    @Override
    public void setRankManager(RankManager rankManager) {
        this.rankManager = rankManager;
        this.logger.info("The previously selected rank manager has been overridden with {}",
                rankManager.getClass().getName());
    }

    @Override
    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public ChatMessenger getChatMessenger() {
        return messenger;
    }

    public static JellyChatsPlugin getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        long start = System.currentTimeMillis();
        init();

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin initialization took {} ms", (end - start));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        long start = System.currentTimeMillis();
        shutdown();

        long end = System.currentTimeMillis();
        if (configuration.getBoolean("show-init-times", true)) {
            logger.info("Plugin shutdown took {} ms", (end - start));
        }
    }
}
