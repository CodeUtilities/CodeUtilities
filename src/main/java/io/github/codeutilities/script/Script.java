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
import io.github.codeutilities.script.execution.ScriptPosStack;
import io.github.codeutilities.script.execution.ScriptTask;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Script {

    private final String name;
    private final List<ScriptPart> parts;
    private final Logger LOGGER;
    private final ScriptContext context = new ScriptContext();
    private File file;
    private boolean disabled;

    public Script(String name, List<ScriptPart> parts, boolean disabled) {
        this.name = name;
        this.parts = parts;
        this.disabled = disabled;
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
                        this.execute(new ScriptTask(new ScriptPosStack(pos+1), event,this));
                    } catch (Exception err) {
                        ChatUtil.sendMessage("Error while invoking event " + se.getType().getName() + " in script " + name + ": " + err.getMessage(), ChatType.FAIL);
                        LOGGER.error("Error while invoking event " + se.getType().getName(), err);
                        err.printStackTrace();
                    }
                }
            }
            pos++;
        }
    }

    public void execute(ScriptTask task) {
        if (disabled) {
            return;
        }
        while (task.stack().peek() < parts.size()) {
            ScriptPart part = parts.get(task.stack().peek());
            if (part instanceof ScriptEvent) {
                return;
            } else if (part instanceof ScriptAction sa) {
                Consumer<Runnable> inner = null;
                if (sa.getType().hasChildren()) {
                    int posCopy = task.stack().peek();
                    inner = (preTask) -> task.schedule(posCopy, preTask);
                    int depth = 0;
                    while (task.stack().peek() < parts.size()) {
                        ScriptPart nextPart = parts.get(task.stack().peek());
                        if (nextPart instanceof ScriptEvent) {
                            task.stack().clear();
                            return;
                        } else if (nextPart instanceof ScriptAction sa2) {
                            if (sa2.getType().hasChildren()) {
                                depth++;
                            } else if (sa2.getType() == ScriptActionType.CLOSE_BRACKET) {
                                depth--;
                                if (depth == 0) {
                                    break;
                                }
                            }
                        } else {
                            throw new IllegalStateException("Unexpected script part type: " + nextPart.getClass().getName());
                        }
                        if (!task.stack().isEmpty()) {
                            task.stack().increase();
                        } else {
                            return;
                        }
                    }
                }
                if(sa.getGroup() == ScriptGroup.CONDITION)
                {
                    if(sa.getType() != ScriptActionType.ELSE)
                    {
                        context.setLastIfResult(false);
                    }
                    context.setScheduleInnerHandler(ctx -> { ctx.context().setLastIfResult(true); });
                }
                else
                {
                    context.setScheduleInnerHandler(null);
                }
                sa.invoke(task.event(), context, inner,task, this);
                if (!task.isRunning()) {
                    return;
                }
                if (sa.getType() == ScriptActionType.CLOSE_BRACKET) {
                    if (task.stack().isEmpty()) {
                        return;
                    } else {
                        task.stack().pop();
                    }
                }
                while(context.isForcedToEndScope())
                {
                    context.forceEndScope(-1);
                    if (task.stack().isEmpty()) {
                        return;
                    } else {
                        task.stack().pop();
                    }
                }
                if(context.isLoopBroken())
                {
                    context.breakLoop(-1);
                    int originalPos = task.stack().peekOriginal();
                    while((task.stack().peekOriginal(1) == originalPos))
                    {
                        if(!task.stack().isEmpty())
                            task.stack().pop();
                    }
                    if(task.stack().isEmpty())
                    {
                        return;
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid script part");
            }
            if (!task.stack().isEmpty()) {
                task.stack().increase();
            } else {
                return;
            }
        }
    }

    public List<ScriptPart> getParts() {
        return parts;
    }

    public String getName() {
        return name;
    }

    public boolean disabled() {
        return disabled;
    }

    public void setDisabled(boolean b) {
        disabled = b;
    }

    public ScriptContext getContext() {
        return context;
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
            boolean disabled = object.has("disabled") && object.get("disabled").getAsBoolean();
            return new Script(name, parts,disabled);
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
            object.addProperty("disabled", src.disabled);
            return object;
        }
    }
}
