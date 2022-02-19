package io.github.codeutilities.script.argument;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.values.ScriptNumberValue;
import io.github.codeutilities.script.values.ScriptValue;
import java.lang.reflect.Type;

public record ScriptNumberArgument(double value) implements ScriptArgument {

    @Override
    public ScriptValue getValue(Event event, ScriptContext context) {
        return new ScriptNumberValue(value);
    }

    public static class Serializer implements JsonSerializer<ScriptNumberArgument> {

        @Override
        public JsonElement serialize(ScriptNumberArgument src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "NUMBER");
            object.addProperty("value", src.value());
            return object;
        }
    }
}
