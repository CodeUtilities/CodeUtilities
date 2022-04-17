package io.github.codeutilities.script.execution;

import io.github.codeutilities.event.IEvent;
import io.github.codeutilities.script.Script;

public class ScriptTask {

    private final ScriptPosStack stack;
    private final IEvent event;
    private boolean running;
    private final Script script;

    public ScriptTask(ScriptPosStack stack, IEvent event, Script script) {
        this.stack = stack;
        this.event = event;
        this.script = script;
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        script.execute(this);
    }

    public ScriptPosStack stack() {
        return stack;
    }

    public IEvent event() {
        return event;
    }

    public boolean isRunning() {
        return running;
    }
}
