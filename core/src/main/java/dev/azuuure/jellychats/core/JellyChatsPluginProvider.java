package dev.azuuure.jellychats.core;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Gives developers an easy way to access the {@link JellyChatsPlugin}
 * implementation that is currently in use.
 *
 * @author azurejelly
 * @since 1.2.0
 */
@UtilityClass
public class JellyChatsPluginProvider {

    /**
     * The {@link JellyChatsPlugin} instance currently in use.
     */
    private static JellyChatsPlugin instance = null;

    /**
     * Provides the {@link JellyChatsPlugin} instance currently
     * in use.
     *
     * @return The {@link JellyChatsPlugin} instance.
     */
    @SuppressWarnings({ "LombokGetterMayBeUsed", "RedundantSuppression" })
    public JellyChatsPlugin getInstance() {
        return instance;
    }

    /**
     * Sets the {@link JellyChatsPlugin} instance which is currently
     * in use.
     *
     * @param plugin A {@link JellyChatsPlugin} instance.
     * @throws IllegalStateException if {@link #instance} was already defined.
     */
    @ApiStatus.Internal
    public void setInstance(@NotNull JellyChatsPlugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("instance is already defined.");
        }

        instance = plugin;
    }
}
