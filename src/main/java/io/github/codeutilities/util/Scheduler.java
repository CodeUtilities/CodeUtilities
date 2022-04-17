package io.github.codeutilities.util;

import io.github.codeutilities.event.EventRegister;
import io.github.codeutilities.event.impl.TickEvent;
import io.github.codeutilities.event.listening.EventWatcher;
import io.github.codeutilities.event.listening.IEventListener;
import io.github.codeutilities.loader.Loadable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.MutablePair;

public class Scheduler implements Loadable {

    private static final List<MutablePair<Integer,Runnable>> tasks = new ArrayList<>();

    @Override
    public void load() {
        EventRegister.getInstance().registerListener(new Scheduler.EventListener());
    }

    public static void schedule(int ticks, Runnable runnable) {
        tasks.add(new MutablePair<>(ticks, runnable));
    }

    public static class EventListener implements IEventListener {

        @EventWatcher
        public void onTick(TickEvent event) {
            for(MutablePair<Integer,Runnable> task : new ArrayList<>(tasks)) {
                task.setLeft(task.getLeft() - 1);
                if(task.getLeft() <= 0) {
                    task.getRight().run();
                    tasks.remove(task);
                }
            }
        }

    }

}
