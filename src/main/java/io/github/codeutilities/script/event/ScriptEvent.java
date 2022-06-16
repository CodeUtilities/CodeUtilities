package io.github.codeutilities.script.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.script.ScriptGroup;
import io.github.codeutilities.script.ScriptPart;
import java.lang.reflect.Type;

public class ScriptEvent implements ScriptPart {

    private final ScriptEventType type;

    public ScriptEvent(ScriptEventType type) {
        this.type = type;
    }

    public ScriptEventType getType() {
        return type;
    }

    @Override
    public ScriptGroup getGroup() {
        return ScriptGroup.EVENT;
    }

    public static class Serializer implements JsonSerializer<ScriptEvent> {

        @Override
        public JsonElement serialize(ScriptEvent src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "event");
            obj.addProperty("event", src.getType().name());
            return obj;
        }
    }

}
