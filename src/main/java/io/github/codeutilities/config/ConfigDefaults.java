package io.github.codeutilities.config;

import com.google.gson.JsonObject;

public class ConfigDefaults {

    public static Config getDefaults() {
        Config config = new Config();

        config.json().addProperty("Simple","abc");
        config.json().addProperty("desc:Simple","Example text");

        JsonObject json = new JsonObject();
        json.addProperty("Text","def");
        json.addProperty("desc:Text","Example text in an object");

        config.json().add("Complex",json);
        config.json().addProperty("desc:Complex", "Example object");

        config.json().addProperty("Bool",true);
        config.json().addProperty("desc:Bool","Example boolean");

        config.json().addProperty("Number",1.23);
        config.json().addProperty("desc:Number","Example number");

        return config;
    }
}
