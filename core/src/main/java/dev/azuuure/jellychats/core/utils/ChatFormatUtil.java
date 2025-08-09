package dev.azuuure.jellychats.core.utils;

import lombok.experimental.UtilityClass;

/**
 * Contains some chat format utility constants.
 *
 * @author azurejelly
 * @since 1.2.0
 */
@UtilityClass
public class ChatFormatUtil {

    /**
     * The default console name to be used in private chat messages.
     */
    public static final String DEFAULT_CONSOLE_NAME = "Console";

    /**
     * The default server to be shown in private chat messages if the connected
     * player somehow manages to not be inside a server or if the message is coming
     * from the console.
     */
    public static final String DEFAULT_SERVER = "N/A";

    /**
     * The default fallback private chat format, in case it is removed from the configuration
     * files.
     */
    public static final String DEFAULT_FALLBACK
            = "<color:#ff59e3>[<chat>] (<server>)</color> <color:#ffabf2><player>:</color> <content>";
}
