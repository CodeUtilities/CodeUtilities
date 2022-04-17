package io.github.codeutilities.event.impl.system;

import io.github.codeutilities.event.ICancellable;
import io.github.codeutilities.event.IEvent;
import net.minecraft.client.util.InputUtil.Key;

public class KeyPressEvent implements IEvent, ICancellable {

    private boolean cancelled = false;

    private final Key key;
    private final int action;

    public KeyPressEvent(Key key, int action) {
        this.key = key;
        this.action = action;
    }

    public Key getKey() {
        return this.key;
    }

    public int getAction() {
        return this.action;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
