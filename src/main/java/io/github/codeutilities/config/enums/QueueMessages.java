package io.github.codeutilities.config.enums;

import io.github.codeutilities.config.Config;

public enum QueueMessages {
    MAIN_CHAT(),
    SIDE_CHAT(),
    TOAST(),
    HIDDEN();

    public static QueueMessages getConfig() {
        return getValue(Config.getConfig().json().get("Queue Messages").getAsString());
    }

    private static QueueMessages getValue(String value) {
        return switch (value) {
            case "Main Chat" -> MAIN_CHAT;
            case "Side Chat" -> SIDE_CHAT;
            case "Toast" -> TOAST;
            case "Hidden" -> HIDDEN;

            default -> MAIN_CHAT;
        };
    }
}