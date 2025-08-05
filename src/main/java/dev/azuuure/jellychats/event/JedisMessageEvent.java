package dev.azuuure.jellychats.event;

public record JedisMessageEvent(String channel, String content) {}
