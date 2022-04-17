package io.github.codeutilities.script.argument;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.IEvent;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.values.ScriptValue;
import java.lang.reflect.Type;

public record ScriptVariableArgument(String name) implements ScriptArgument{

    @Override
    public ScriptValue getValue(IEvent event, ScriptContext context) {
        return context.getVariable(name);
    }

    public static class Serializer implements JsonSerializer<ScriptVariableArgument> {

        @Override
        public JsonElement serialize(ScriptVariableArgument src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "VARIABLE");
            object.addProperty("value", src.name());
            return object;
        }
    }
}
