package io.github.codeutilities.script.execution;

import java.util.ArrayList;
import java.util.List;

public class ScriptPosStack {

    private final List<Integer> data = new ArrayList<>();
    private final List<Runnable> preTasks = new ArrayList<>();

    public ScriptPosStack(int initial) {
        data.add(initial);
    }

    public void push(int value, Runnable preTask) {
        data.add(value);
        preTasks.add(preTask);
    }

    public void push(int value) {
        data.add(value);
        preTasks.add(null);
    }

    public void pop() {
        Runnable preTask = preTasks.remove(preTasks.size() - 1);
        if (preTask != null) {
            preTask.run();
        }
        data.remove(data.size() - 1);
    }

    public int peek() {
        return data.get(data.size() - 1);
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        data.clear();
    }

    public void increase() {
        data.set(data.size() - 1, data.get(data.size() - 1) + 1);
    }
}
