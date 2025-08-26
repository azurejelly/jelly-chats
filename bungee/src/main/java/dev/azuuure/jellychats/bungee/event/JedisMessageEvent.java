package dev.azuuure.jellychats.bungee.event;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

@Getter
public class JedisMessageEvent extends Event {

    private final String channel;
    private final String message;

    public JedisMessageEvent(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }
}
