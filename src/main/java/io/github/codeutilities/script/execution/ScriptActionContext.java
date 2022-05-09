package io.github.codeutilities.script.execution;

import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import io.github.codeutilities.script.values.ScriptValue;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record ScriptActionContext(ScriptContext context, List<ScriptArgument> arguments, Event event, Consumer<Runnable> inner, ScriptTask task, HashMap<String, List<ScriptArgument>> argMap, Script script) {

    public void setArg(String name, List<ScriptArgument> args) {
        argMap.put(name, args);
    }

    public List<ScriptArgument> pluralArg(String messages) {
        return argMap.get(messages);
    }

    public ScriptArgument arg(String name) {
        return argMap.get(name).get(0);
    }

    public ScriptValue value(String name) {
        return arg(name).getValue(event,context);
    }

    public List<ScriptValue> pluralValue(String name) {
        return pluralArg(name).stream().map(arg -> arg.getValue(event,context)).collect(Collectors.toList());
    }

    public ScriptVariableArgument variable(String name) {
        return (ScriptVariableArgument) arg(name);
    }

    public void scheduleInner(Runnable runnable) {
        context.invokeScheduleInnerHandler(this);
        inner.accept(runnable);
    }

    public void scheduleInner() {
        context.invokeScheduleInnerHandler(this);
        inner.accept(null);
    }
}
