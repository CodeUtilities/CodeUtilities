package io.github.codeutilities.script.execution;

import java.util.function.Consumer;

public class ScriptPosStackElement {
    private int pos;
    private int originalPos;
    private Runnable preTask = null;

    private Consumer<ScriptContext> condition = null;

    public ScriptPosStackElement(int initial) {
        originalPos = initial;
        setPos(originalPos);
    }

    public ScriptPosStackElement(int initial, Runnable preTask) {
        originalPos = initial;
        setPos(originalPos);
        this.preTask = preTask;
    }
    public ScriptPosStackElement(int initial, Runnable preTask, Consumer<ScriptContext> condition) {
        originalPos = initial;
        setPos(originalPos);
        this.preTask = preTask;
        this.condition = condition;
    }
    public ScriptPosStackElement setPos(int pos) {
        this.pos = pos;
        return this;
    }
    public int getPos() {
        return pos;
    }
    public int getOriginalPos() {
        return originalPos;
    }
    public void runPreTask() {
        if (preTask != null) {
            preTask.run();
        }
    }
    public boolean checkCondition(ScriptContext ctx) {
        if(condition == null)
        {
            return false;
        }

        ctx.setLastIfResult(false);

        condition.accept(ctx);
        return ctx.lastIfResult();
    }

    public boolean hasCondition() {
        return condition != null;
    }
}