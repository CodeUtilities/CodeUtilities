package io.github.codeutilities.config;

import com.google.gson.JsonObject;

public class ConfigDefaults {

    public static Config getDefaults() {
        Config config = new Config();

        config.json().addProperty("simple","abc");

        JsonObject json = new JsonObject();
        json.addProperty("complex","def");

        config.json().add("complex",json);

        return config;
    }

}
