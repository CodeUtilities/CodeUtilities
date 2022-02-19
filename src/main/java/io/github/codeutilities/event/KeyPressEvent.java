package io.github.codeutilities.event;

import com.mojang.blaze3d.platform.InputConstants.Key;
import io.github.codeutilities.event.system.CancellableEvent;

public class KeyPressEvent implements CancellableEvent {
    private boolean cancelled = false;
    private final Key key;
    private final int action;

    public KeyPressEvent(Key key, int action) {
        this.key = key;
        this.action = action;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public Key getKey() {
        return key;
    }

    public int getAction() {
        return action;
    }
}
