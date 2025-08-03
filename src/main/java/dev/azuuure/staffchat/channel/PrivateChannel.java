package dev.azuuure.staffchat.channel;

/**
 * Represents the type of private channel a player is typing on.
 *
 * @author azurejelly
 */
public enum PrivateChannel {

    STAFF("staff"),
    ADMIN("admin");

    private final String type;

    PrivateChannel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return "staff-chat/" + getType();
    }

    public String getPermission() {
        return "staff-chat.channel." + type;
    }

    public static PrivateChannel fromChannel(String channel) {
        for (PrivateChannel ch : PrivateChannel.values()) {
            if (!ch.getChannel().equals(channel)) {
                continue;
            }

            return ch;
        }

        return null;
    }
}
