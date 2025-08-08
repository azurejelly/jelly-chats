package dev.azuuure.jellychats.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@UtilityClass
public class ComponentUtil {

    public static final char LEGACY_SECTION_SYMBOL = '§';

    public String replaceSectionSymbols(String str) {
        return str.replace(LEGACY_SECTION_SYMBOL, '&');
    }

    public Component toComponent(String str, boolean useLegacy) {
        str = replaceSectionSymbols(str);

        if (useLegacy) {
            try {
                return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
            } catch (RuntimeException ex) {
                return Component.text(str);
            }
        } else {
            try {
                return MiniMessage.miniMessage().deserialize(str);
            } catch (RuntimeException ex) {
                return Component.text(str);
            }
        }
    }
}
