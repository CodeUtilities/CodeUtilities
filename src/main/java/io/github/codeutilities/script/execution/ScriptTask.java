package io.github.codeutilities.script.execution;

import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.Script;
import org.jetbrains.annotations.Debug;

import static io.github.codeutilities.CodeUtilities.LOGGER;

public class ScriptTask {

    private final ScriptPosStack stack;
    private final Event event;
    private boolean running;
    private final Script script;

    public ScriptTask(ScriptPosStack stack, Event event, Script script) {
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

    public Event event() {
        return event;
    }

    public boolean isRunning() {
        return running;
    }

    public void schedule(int posCopy, Runnable preTask) {
        stack.push(posCopy, preTask);
    }
}
