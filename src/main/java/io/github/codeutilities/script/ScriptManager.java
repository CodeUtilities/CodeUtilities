package io.github.codeutilities.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.codeutilities.event.BuildModeEvent;
import io.github.codeutilities.event.DevModeEvent;
import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.PlayModeEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.SendChatEvent;
import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.argument.ScriptClientValueArgument;
import io.github.codeutilities.script.argument.ScriptNumberArgument;
import io.github.codeutilities.script.argument.ScriptTextArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import io.github.codeutilities.script.event.ScriptEvent;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScriptManager implements Loadable {

    private static final Logger LOGGER = LogManager.getLogger("CuModules");
    private static ScriptManager instance;
    private final List<Script> scripts = new ArrayList<>();
    private final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(Script.class, new Script.Serializer())
        .registerTypeAdapter(ScriptPart.class, new ScriptPart.Serializer())
        .registerTypeAdapter(ScriptArgument.class, new ScriptArgument.Serializer())
        .registerTypeAdapter(ScriptTextArgument.class, new ScriptTextArgument.Serializer())
        .registerTypeAdapter(ScriptNumberArgument.class, new ScriptNumberArgument.Serializer())
        .registerTypeAdapter(ScriptVariableArgument.class, new ScriptVariableArgument.Serializer())
        .registerTypeAdapter(ScriptClientValueArgument.class, new ScriptClientValueArgument.Serializer())
        .registerTypeAdapter(ScriptAction.class, new ScriptAction.Serializer())
        .registerTypeAdapter(ScriptEvent.class, new ScriptEvent.Serializer())
        .create();

    public ScriptManager() {
        instance = this;
    }

    public static ScriptManager getInstance() {
        if (instance == null) {
            instance = new ScriptManager();
        }
        return instance;
    }

    @Override
    public void load() {
        loadScripts();
        loadEvents();
    }

    private void loadScripts() {
        LOGGER.info("Loading script...");

        File dir = FileUtil.cuFolder("Scripts").toFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOGGER.error("Failed to create script directory!");
                return;
            }
        }

        for (File file : dir.listFiles()) {
            try {
                if (file.isDirectory()) {
                    LOGGER.info("Skipped directory: " + file.getName());
                    continue;
                }
                String content = FileUtil.readFile(file.toPath());
                Script s = GSON.fromJson(content, Script.class);
                s.setFile(file);
                scripts.add(s);
                LOGGER.info("Loaded script: " + file.getName());
            } catch (Exception e) {
                LOGGER.error("Failed to load script: " + file.getName());
                e.printStackTrace();
            }
        }

        LOGGER.info("Loaded " + scripts.size() + " script!");
    }

    public void saveScript(Script script) {
        try {
            FileUtil.writeFile(script.getFile().toPath(), GSON.toJson(script));
        } catch (Exception e) {
            LOGGER.error("Failed to save script: " + script.getFile().getName());
            e.printStackTrace();
        }
    }

    private void loadEvents() {
        EventManager manager = EventManager.getInstance();

        manager.register(SendChatEvent.class, this::handleEvent);
        manager.register(KeyPressEvent.class, this::handleEvent);
        manager.register(ReceiveChatEvent.class, this::handleEvent);
        manager.register(TickEvent.class, this::handleEvent);
        manager.register(PlayModeEvent.class, this::handleEvent);
        manager.register(BuildModeEvent.class, this::handleEvent);
        manager.register(DevModeEvent.class, this::handleEvent);
    }

    private void handleEvent(Event event) {
        for (Script script : scripts) {
            script.invoke(event);
        }
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void deleteScript(Script script) {
        if (scripts.contains(script)) {
            scripts.remove(script);
            if (!script.getFile().delete()) {
                LOGGER.error("Failed to delete script: " + script.getFile().getName());
            }
        }
    }

    public void createScript(String name) {
        Script script = new Script(name, new ArrayList<>());
        scripts.add(script);
        File file = FileUtil.cuFolder("Scripts").resolve(name + ".json").toFile();
        script.setFile(file);
        saveScript(script);
    }
}
