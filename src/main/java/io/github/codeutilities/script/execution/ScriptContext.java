package io.github.codeutilities.script.execution;

import io.github.codeutilities.script.values.ScriptUnknownValue;
import io.github.codeutilities.script.values.ScriptValue;
import java.util.HashMap;

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

}
