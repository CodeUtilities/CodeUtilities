package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.ICancellable;
import io.github.codeutilities.event.IEvent;
import net.minecraft.text.Text;

public class ChatReceivedEvent implements IEvent, ICancellable {

    private final Text message;
    private boolean cancelled = false;

    public ChatReceivedEvent(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return this.message;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
