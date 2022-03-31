package io.github.codeutilities.event;

import io.github.codeutilities.event.system.CancellableEvent;
import net.minecraft.text.Text;

public class ReceiveChatEvent implements CancellableEvent {
    private final Text message;
    private boolean cancelled = false;

    public ReceiveChatEvent(Text message) {
        this.message = message;
    }

    public Text getMessage() {
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
