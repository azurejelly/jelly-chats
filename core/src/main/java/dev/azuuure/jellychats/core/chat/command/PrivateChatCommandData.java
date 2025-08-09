package dev.azuuure.jellychats.core.chat.command;

import dev.azuuure.jellychats.core.chat.PrivateChat;
import lombok.Builder;

import java.util.List;

/**
 * Contains command data for each {@link PrivateChat}.
 * <p>
 * For the permission of each command, use {@link PrivateChat#permission()} instead.
 *
 * @param main The main command alias.
 * @param aliases Other secondary aliases.
 * @since 1.2.0
 * @author azurejelly
 */
@Builder
public record PrivateChatCommandData(String main, List<String> aliases) {}
