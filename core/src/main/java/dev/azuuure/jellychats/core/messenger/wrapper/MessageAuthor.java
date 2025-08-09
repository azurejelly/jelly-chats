package dev.azuuure.jellychats.core.messenger.wrapper;

import dev.azuuure.jellychats.core.utils.ChatFormatUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(fluent = true)
public final class MessageAuthor {

    private final UUID uuid;
    private final String name;
    private final String server;

    private MessageAuthor(UUID uuid, String name, String server) {
        this.uuid = uuid;
        this.server = server;
        this.name = name;
    }

    private MessageAuthor(String name, String server) {
        this.server = server;
        this.uuid = null;
        this.name = name;
    }

    public static MessageAuthor from(UUID uuid, String name, String server) {
        return new MessageAuthor(uuid, server, name);
    }

    public static MessageAuthor asConsole(String name, String server) {
        return new MessageAuthor(name, server);
    }

    public static MessageAuthor asConsole(String name) {
        return new MessageAuthor(name, ChatFormatUtil.DEFAULT_SERVER);
    }

    public static MessageAuthor asConsole() {
        return new MessageAuthor(
                ChatFormatUtil.DEFAULT_CONSOLE_NAME,
                ChatFormatUtil.DEFAULT_SERVER
        );
    }

    public boolean isConsole() {
        return this.uuid == null;
    }
}
