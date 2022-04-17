package io.github.codeutilities.event;

public interface ICancellable {

    void setCancelled(boolean b);

    boolean isCancelled();

}
