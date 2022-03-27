package io.github.codeutilities.script;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.event.ScriptEvent;
import io.github.codeutilities.script.execution.ScriptContext;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Script {

    private final String name;
    private final List<ScriptPart> parts;
    private final Logger LOGGER;
    private final ScriptContext context = new ScriptContext();
    private File file;

    public Script(String name, List<ScriptPart> parts) {
        this.name = name;
        this.parts = parts;
        LOGGER = LogManager.getLogger("Script." + name);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void invoke(Event event) {
        int pos = 0;
        for (ScriptPart part : parts) {
            if (part instanceof ScriptEvent se) {
                if (se.getType().getCodeutilitiesEvent().equals(event.getClass())) {
                    try {
                        execute(pos + 1, event);
                    } catch (Exception err) {
                        ChatUtil.error("Error while invoking event " + se.getType().getName() + " in script " + name + ": " + err.getMessage());
                        LOGGER.error("Error while invoking event " + se.getType().getName(), err);
                        err.printStackTrace();
                    }
                }
            }
            pos++;
        }
    }

    private void execute(int pos, Event event) {
        while (pos < parts.size()) {
            ScriptPart part = parts.get(pos);
            if (part instanceof ScriptEvent) {
                return;
            } else if (part instanceof ScriptAction sa) {
                Runnable inner = null;
                if (sa.getType().hasChildren()) {
                    int nextPos = pos + 1;
                    inner = () -> execute(nextPos, event);
                    int depth = 0;
                    while (pos < parts.size()) {
                        ScriptPart nextPart = parts.get(pos);
                        if (nextPart instanceof ScriptEvent) {
                            pos = Integer.MAX_VALUE;
                        } else if (nextPart instanceof ScriptAction sa2) {
                            if (sa2.getType().hasChildren()) {
                                depth++;
                            } else if (sa2.getType() == ScriptActionType.CLOSE_BRACKET) {
                                if (depth == 0) {
                                    pos++;
                                    break;
                                }
                                depth--;
                            }
                        } else {
                            throw new IllegalStateException("Unexpected script part type: " + nextPart.getClass().getName());
                        }
                        pos++;
                    }
                }
                sa.invoke(event, context, inner);
                if (sa.getType() == ScriptActionType.CLOSE_BRACKET) {
                    return;
                }
            } else {
                throw new IllegalArgumentException("Invalid script part");
            }
            pos++;
        }
    }

    public List<ScriptPart> getParts() {
        return parts;
    }

    public String getName() {
        return name;
    }

    public static class Serializer implements JsonSerializer<Script>, JsonDeserializer<Script> {

        @Override
        public Script deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String name = object.get("name").getAsString();
            List<ScriptPart> parts = new ArrayList<>();
            for (JsonElement element : object.get("actions").getAsJsonArray()) {
                ScriptPart part = context.deserialize(element, ScriptPart.class);
                parts.add(part);
            }
            return new Script(name, parts);
        }

        @Override
        public JsonElement serialize(Script src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("name", src.name);
            JsonArray array = new JsonArray();
            for (ScriptPart part : src.getParts()) {
                array.add(context.serialize(part));
            }
            object.add("actions", array);
            return object;
        }
    }
}
