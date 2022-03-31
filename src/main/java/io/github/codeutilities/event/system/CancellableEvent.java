package io.github.codeutilities.event.system;

public interface CancellableEvent extends Event {
    void setCancelled(boolean cancel);

    boolean isCancelled();
}
