package de.vinnie.network;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentManager {

    private static volatile ComponentManager componentManager;

    private ComponentManager() {}

    public static ComponentManager getComponentManager() {
        if (componentManager == null) {
            synchronized (ComponentManager.class) {
                if (componentManager == null) {
                    componentManager = new ComponentManager();
                }
            }
        }
        return componentManager;
    }

    public String serializeToLegacy(String string) {
        Component textComponent = MiniMessage.miniMessage().deserialize(string);
        return LegacyComponentSerializer.legacySection().serialize(textComponent);
    }

    public Component deserializeMiniMessage(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }
}
