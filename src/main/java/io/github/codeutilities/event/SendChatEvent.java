package io.github.codeutilities.event;

import io.github.codeutilities.event.system.CancellableEvent;

public class SendChatEvent implements CancellableEvent {
    private boolean cancelled = false;
    private final String message;

    public SendChatEvent(String message) {
        this.message = message;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public String getMessage() {
        return message;
    }
}
