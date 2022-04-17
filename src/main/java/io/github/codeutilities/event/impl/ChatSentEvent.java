package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.ICancellable;
import io.github.codeutilities.event.IEvent;

public class ChatSentEvent implements IEvent, ICancellable {

    private boolean cancelled = false;
    private final String message;

    public ChatSentEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

}
