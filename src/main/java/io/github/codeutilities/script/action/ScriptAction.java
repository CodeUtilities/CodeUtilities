package io.github.codeutilities.script.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptGroup;
import io.github.codeutilities.script.ScriptPart;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.execution.ScriptActionContext;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.execution.ScriptTask;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ScriptAction implements ScriptPart {

    private final ScriptActionType type;
    private final List<ScriptArgument> arguments;

    public ScriptAction(ScriptActionType type, List<ScriptArgument> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public void invoke(Event event, ScriptContext context, BiConsumer<Runnable, Consumer<ScriptContext>> inner, ScriptTask task, Script script) {
        type.run(new ScriptActionContext(
            context, arguments, event, inner, task, new HashMap<>(), script
        ));
    }

    public ScriptActionType getType() {
        return type;
    }

    public List<ScriptArgument> getArguments() {
        return arguments;
    }

    @Override
    public ScriptGroup getGroup() {
        return getType().getGroup();
    }

    public static class Serializer implements JsonSerializer<ScriptAction> {

        @Override
        public JsonElement serialize(ScriptAction src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "action");
            obj.addProperty("action", src.getType().name());
            obj.add("arguments", context.serialize(src.getArguments()));
            return obj;
        }
    }
}