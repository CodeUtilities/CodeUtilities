package io.github.codeutilities.script.execution;

import io.github.codeutilities.script.values.ScriptUnknownValue;
import io.github.codeutilities.script.values.ScriptValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ScriptContext {

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
