package dev.azuuure.jellychats.core.messenger.wrapper;

import dev.azuuure.jellychats.core.utils.ChatFormatUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents the author of a private chat message.
 *
 * @author azurejelly
 * @since 1.2.0
 */
@Data
@Accessors(fluent = true)
public final class MessageAuthor {

    /**
     * The {@link UUID} of the author.
     */
    @Nullable private final UUID uuid;
    @NotNull private final String name;
    @NotNull private final String server;

    private MessageAuthor(@Nullable UUID uuid, @NotNull String name, @NotNull String server) {
        this.uuid = uuid;
        this.server = server;
        this.name = name;
    }

    private MessageAuthor(@NotNull String name, @NotNull String server) {
        this.uuid = null;
        this.name = name;
        this.server = server;
    }

    /**
     * Creates a {@link MessageAuthor} with the specified parameters.
     *
     * @param uuid   The {@link UUID} of the author, which might only
     *               be null if the author is the console.
     * @param name   The name of the author.
     * @param server The server where the author is in. Will default
     *               to {@link ChatFormatUtil#DEFAULT_SERVER} if null.
     * @return A {@link MessageAuthor} with the specified parameters.
     */
    public static MessageAuthor from(@Nullable UUID uuid, @NotNull String name, @Nullable String server) {
        if (server == null) {
            server = ChatFormatUtil.DEFAULT_SERVER;
        }

        return new MessageAuthor(uuid, server, name);
    }

    /**
     * Creates a {@link MessageAuthor} with the specified parameters.
     * <p>
     * The server of the author will fall back to the default value
     * stored at {@link ChatFormatUtil#DEFAULT_SERVER}.
     *
     * @param uuid The {@link UUID} of the author, which might only
     *             be null if the author is the console.
     * @param name The name of the author.
     * @return A {@link MessageAuthor} with the specified parameters.
     */
    public static MessageAuthor from(@Nullable UUID uuid, @NotNull String name) {
        return new MessageAuthor(uuid, ChatFormatUtil.DEFAULT_SERVER, name);
    }

    /**
     * Creates a {@link MessageAuthor} in a server console context
     * with the specified parameters.
     *
     * @param name   The name the server console should have.
     *               Will default to {@link ChatFormatUtil#DEFAULT_CONSOLE_NAME}
     *               if null.
     * @param server The server the console should be 'connected to'.
     *               Will default to {@link ChatFormatUtil#DEFAULT_SERVER}
     *               if null.
     * @return A {@link MessageAuthor} with the specified parameters.
     */
    public static MessageAuthor asConsole(@Nullable String name, @Nullable String server) {
        if (name == null) {
            name = ChatFormatUtil.DEFAULT_CONSOLE_NAME;
        }

        if (server == null) {
            server = ChatFormatUtil.DEFAULT_SERVER;
        }

        return new MessageAuthor(name, server);
    }

    /**
     * Creates a {@link MessageAuthor} in a server console context
     * with the specified parameters.
     * <p>
     * The server the console is 'connected to' will fall back to the
     * default value stored at {@link ChatFormatUtil#DEFAULT_SERVER}.
     *
     * @param name The name the server console should have.
     *             Will default to {@link ChatFormatUtil#DEFAULT_CONSOLE_NAME}
     *             if null.
     * @return A {@link MessageAuthor} with the specified parameters.
     */
    public static MessageAuthor asConsole(String name) {
        return new MessageAuthor(name, ChatFormatUtil.DEFAULT_SERVER);
    }

    /**
     * Creates a {@link MessageAuthor} in a server console context.
     * <p>
     * Both the server the console is 'connected to' and the name
     * it should use in each private chat message will fall back
     * to the default {@link ChatFormatUtil#DEFAULT_CONSOLE_NAME}
     * and {@link ChatFormatUtil#DEFAULT_SERVER} values, respectively.
     *
     * @return A {@link MessageAuthor} in a server console context.
     */
    public static MessageAuthor asConsole() {
        return new MessageAuthor(
                ChatFormatUtil.DEFAULT_CONSOLE_NAME,
                ChatFormatUtil.DEFAULT_SERVER
        );
    }

    /**
     * Whether the author is the server console. This is the case
     * when {@link #uuid()} returns null.
     *
     * @return true if the author is the server console.
     */
    public boolean isConsole() {
        return this.uuid == null;
    }
}
