package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import dev.azuuure.jellychats.core.JellyChatsPlugin;

public class PluginModule extends AbstractModule {

    private final JellyChatsPlugin plugin;

    public PluginModule(JellyChatsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(JellyChatsPlugin.class).toInstance(plugin);
    }
}
