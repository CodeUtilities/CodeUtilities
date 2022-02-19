package io.github.codeutilities.event;

import io.github.codeutilities.event.system.CancellableEvent;
import net.minecraft.network.chat.Component;

public class ReceiveChatEvent implements CancellableEvent {

    private final Component message;
    private boolean cancelled = false;

    public ReceiveChatEvent(Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
