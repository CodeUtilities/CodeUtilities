package io.github.codeutilities.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ConfigDefaults {

    public static Config getDefaults() {
        Config config = new Config();
        JsonObject root = config.json();

        stringSetting(root,"Simple","abc", "A simple text setting");

        JsonObject json = new JsonObject();
        stringSetting(json,"Text","yeet","Example text in an object");

        objectSetting(root,"Complex",json,"Example object");

        booleanSetting(root,"Bool",true,"Example boolean");

        numberSetting(root,"Number",1.23,"Example number");

        enumSetting(root,"Enum","Good","Example enum", "Good", "Bad");

        return config;
    }

    private static void stringSetting(JsonObject obj, String name, String value, String desc) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static void objectSetting(JsonObject obj, String name, JsonObject json, String desc) {
        obj.add(name, json);
        obj.addProperty("desc:" + name, desc);
    }

    private static void booleanSetting(JsonObject obj, String name, boolean value, String desc) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static void numberSetting(JsonObject obj, String name, double value, String desc) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static void enumSetting(JsonObject obj, String name, String value, String desc, String... values) {
        JsonArray array = new JsonArray();
        for(String s : values) {
            array.add(s);
        }
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
        obj.add("options:" + name, array);
    }
}
