package io.github.codeutilities.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.FileUtil;

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
        try {
            FileUtil.writeFile(FileUtil.cuFolder("config.json"), data.toString());
            CodeUtilities.LOGGER.info("Saved config!");
        } catch (Exception err) {
            CodeUtilities.LOGGER.error("Failed to save config!");
            err.printStackTrace();
        }
    }

    public void merge(Config other) {
        for (String key : other.json().keySet()) {
            if (!data.has(key)) {
                if (data.isJsonObject()){
                    subMerge(data.getAsJsonObject(), other.json().getAsJsonObject());
                } else {
                    data.add(key, other.json().get(key));
                }
            }
        }
    }

    private void subMerge(JsonObject a, JsonObject b) {
        for (String key : b.keySet()) {
            if (!a.has(key)) {
                if (a.isJsonObject()) {
                    subMerge(a.getAsJsonObject(), b.getAsJsonObject());
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
