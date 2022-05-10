package io.github.codeutilities.script.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScriptPosStack {

    private final List<ScriptPosStackElement> data = new ArrayList<>();
    public ScriptPosStack(int initial) {
        push(initial);
    }

    public void push(int value, Runnable preTask) {
        data.add(new ScriptPosStackElement(value, preTask));
    }

    public void push(int value, Runnable preTask, Consumer<ScriptContext> condition) {
        data.add(new ScriptPosStackElement(value, preTask, condition));
    }
    public void push(int value) {
        data.add(new ScriptPosStackElement(value));
    }

    public void pop() {
        if(isEmpty())
        {
            return;
        }
        ScriptPosStackElement element = data.remove(data.size() - 1);
        element.runPreTask();
    }

    public int peek() {
        return peek(0);
    }

    public int peekOriginal() {
        return peekOriginal(0);
    }

    public ScriptPosStackElement peekElement() { return peekElement(0); }

    public int peek(int n)
    {
        ScriptPosStackElement element = peekElement(n);
        if(element == null)
        {
            return -1;
        }
        return element.getPos();
    }

    public int peekOriginal(int n)
    {
        ScriptPosStackElement element = peekElement(n);
        if(element == null)
        {
            return -1;
        }
        return element.getOriginalPos();
    }

    public ScriptPosStackElement peekElement(int n) {
        if(!(data.size() - 1 - n >= 0))
        {
            return null;
        }
        return data.get(data.size() - 1 - n);
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
        ScriptPosStackElement element = peekElement();
        data.set(data.size() - 1, element.setPos(element.getPos()+1));
    }
}
