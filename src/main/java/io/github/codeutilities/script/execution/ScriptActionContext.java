package io.github.codeutilities.script.execution;

import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import io.github.codeutilities.script.values.ScriptValue;
import java.util.List;

public record ScriptActionContext(ScriptContext context, List<ScriptArgument> arguments, Event event, Runnable inner, ScriptTask task) {

    public ScriptValue argValue(int i) {
        return arguments.get(i).getValue(event, context);
    }

    public ScriptVariableArgument argVariable(int i) {
        if (arguments.get(i) instanceof ScriptVariableArgument sva) {
            return sva;
        } else {
            throw new IllegalArgumentException("Argument " + i + " is not a variable");
        }
    }

    public void minArguments(int count) {
        if (arguments().size() < count) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected at least " + count + " but got " + arguments().size());
        }
    }

    public void maxArguments(int count) {
        if (arguments().size() > count) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected at most " + count + " but got " + arguments().size());
        }
    }

    public void exactArguments(int count) {
        if (arguments().size() != count) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected " + count + " but got " + arguments().size());
        }
    }
}
