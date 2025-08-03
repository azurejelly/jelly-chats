package dev.azuuure.staffchat.event;

public record JedisMessageEvent(String channel, String content) {}
