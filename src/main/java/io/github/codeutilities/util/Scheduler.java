package io.github.codeutilities.util;

import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.MutablePair;

public class Scheduler implements Loadable {

    private static final List<MutablePair<Integer,Runnable>> tasks = new ArrayList<>();

    @Override
    public void load() {
        EventManager.getInstance().register(TickEvent.class, (e) -> {
            for(MutablePair<Integer,Runnable> task : new ArrayList<>(tasks)) {
                task.setLeft(task.getLeft() - 1);
                if(task.getLeft() <= 0) {
                    task.getRight().run();
                    tasks.remove(task);
                }
            }
        });
    }

    public static void schedule(int ticks, Runnable runnable) {
        tasks.add(new MutablePair<>(ticks, runnable));
    }
}
