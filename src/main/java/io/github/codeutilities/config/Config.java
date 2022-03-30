package io.github.codeutilities.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.FileUtil;
import java.util.List;

public class Config {

    private JsonObject data;

    public Config() {
        data = new JsonObject();
    }

    public void loadFromFile() {
        if (FileUtil.cuFolder("config.json").toFile().exists()) {
            try {
                data = JsonParser.parseString(FileUtil.readFile(FileUtil.cuFolder("config.json"))).getAsJsonObject();
                CodeUtilities.LOGGER.info("Loaded config!");
            } catch (Exception err) {
                CodeUtilities.LOGGER.error("Failed to load config!");
                err.printStackTrace();
            }
        }
    }

    public void saveToFile() {
        JsonObject saveData = new JsonObject();
        save(saveData, data);

        try {
            FileUtil.writeFile(FileUtil.cuFolder("config.json"), saveData.toString());
            CodeUtilities.LOGGER.info("Saved config!");
        } catch (Exception err) {
            CodeUtilities.LOGGER.error("Failed to save config!");
            err.printStackTrace();
        }
    }

    private void save(JsonObject to, JsonObject from) {
        for (String key : from.keySet()) {
            if (ConfigManager.INFO_PREFIXES.stream().anyMatch(key::startsWith)) {
                continue;
            }
            if (from.get(key).isJsonObject()) {
                JsonObject sub = new JsonObject();
                save(sub, from.getAsJsonObject(key));
                to.add(key, sub);
            } else {
                to.add(key, from.get(key));
            }
        }
    }

    public void merge(Config other) {
        subMerge(data, other.json());
    }

    private void subMerge(JsonObject a, JsonObject b) {
        for (String key : b.keySet()) {
            if (!a.has(key)) {
                if (b.get(key).isJsonObject()) {
                    JsonObject sub = new JsonObject();
                    subMerge(sub, b.getAsJsonObject(key));
                    a.add(key, sub);
                } else {
                    a.add(key, b.get(key));
                }
            }
        }
    }

    public JsonObject json() {
        return data;
    }
}
