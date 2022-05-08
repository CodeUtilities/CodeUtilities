package io.github.codeutilities.script.execution;

import java.util.ArrayList;
import java.util.List;

public class ScriptPosStack {

    private final List<Integer> data = new ArrayList<>();
    private final List<Integer> originalData = new ArrayList<>();
    private final List<Runnable> preTasks = new ArrayList<>();

    public ScriptPosStack(int initial) {
        data.add(initial);
        originalData.add(initial);
        preTasks.add(null);
    }

    public void push(int value, Runnable preTask) {
        data.add(value);
        originalData.add(value);
        preTasks.add(preTask);
    }

    public void push(int value) {
        data.add(value);
        originalData.add(value);
        preTasks.add(null);
    }

    public void pop() {
        if(isEmpty())
        {
            return;
        }
        Runnable preTask = preTasks.remove(preTasks.size() - 1);
        if (preTask != null) {
            preTask.run();
        }
        originalData.remove(originalData.size() - 1);
        data.remove(data.size() - 1);
    }

    public int peek() {
        return peek(0);
    }

    public int peekOriginal() {
        return peekOriginal(0);
    }

    public int peek(int n)
    {
        if(!(data.size() - 1 - n >= 0))
        {
            return -1;
        }
        return data.get(data.size() - 1 - n);
    }

    public int peekOriginal(int n)
    {
        if(originalData.size() - 1 - n < 0)
        {
            return -1;
        }
        return originalData.get(originalData.size() - 1 - n);
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        data.clear();
        originalData.clear();
        preTasks.clear();
    }

    public void increase() {
        data.set(data.size() - 1, data.get(data.size() - 1) + 1);
    }
}
