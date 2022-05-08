package io.github.codeutilities.script.execution;

import io.github.codeutilities.script.values.ScriptUnknownValue;
import io.github.codeutilities.script.values.ScriptValue;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static io.github.codeutilities.CodeUtilities.LOGGER;

public class ScriptContext {

    private int isForcedToEnd = 0;

    public void forceEndScope(int times) {
        isForcedToEnd += times;
    }

    public void forceEndScope() {
        isForcedToEnd++;
    }

    public void stopEndScope()
    {
        isForcedToEnd = 0;
    }

    public boolean isForcedToEndScope()
    {
        return isForcedToEnd > 0;
    }

    private int breakLoop = 0;

    public void breakLoop(int n)
    {
        breakLoop += n;
    }

    public void breakLoop()
    {
        breakLoop(1);
    }

    public void stopBreakLoop()
    {
        breakLoop = 0;
    }

    public boolean isLoopBroken()
    {
        return breakLoop > 0;
    }

    private final HashMap<String,ScriptValue> variables = new HashMap<>();

    public ScriptValue getVariable(String name) {
        if (!variables.containsKey(name)) {
            return new ScriptUnknownValue();
        }
        return variables.get(name);
    }

    public void setVariable(String name, ScriptValue value) {
        variables.put(name, value);
    }

    public List<Entry<String, ScriptValue>> listVariables(String filter) {
        return variables.entrySet().stream().filter(entry -> entry.getKey().contains(filter)).collect(Collectors.toList());
    }

    public int getVariableCount() {
        return variables.size();
    }
}
