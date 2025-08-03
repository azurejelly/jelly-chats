package dev.azuuure.staffchat.event;

import dev.azuuure.staffchat.channel.PrivateChannel;
import dev.azuuure.staffchat.channel.message.PrivateChannelMessage;

public record PrivateChannelMessageEvent(PrivateChannel channel, PrivateChannelMessage message) {}
