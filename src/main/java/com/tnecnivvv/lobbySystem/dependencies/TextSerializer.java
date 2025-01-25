package com.tnecnivvv.lobbySystem.dependencies;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextSerializer {

    private final MiniMessage miniMessage;

    public TextSerializer() {
        miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Serializes a Component to a MiniMessage format string
     *
     * @param component The `Component` object to serialize
     * @return A MiniMessage formatted string representing the component
     */
    public String serializeToString(Component component) {
        return miniMessage.serialize(component);
    }

    /**
     * Deserializes a MiniMessage formatted string to a Component object
     *
     * @param string The MiniMessage formatted string to deserialize
     * @return A `Component` representing the MiniMessage formatted string
     */
    public Component deserializeToComponent(String string) {
        return miniMessage.deserialize(string);
    }
}