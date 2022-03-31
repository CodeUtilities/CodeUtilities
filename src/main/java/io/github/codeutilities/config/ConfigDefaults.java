package io.github.codeutilities.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.codeutilities.util.hypercube.rank.HypercubeRank;

public class ConfigDefaults {

    public static JsonObject getDefaults() {
        JsonObject root = new JsonObject();

        /*
        stringSetting(root,"Simple", "abc", "A simple text setting", HypercubeRank.DEFAULT);
        JsonObject json = new JsonObject();
        stringSetting(json, "Text","yeet", "Example text in an object", HypercubeRank.DEFAULT);
        objectSetting(root, "Complex", json, "Example object", HypercubeRank.DEFAULT);
        booleanSetting(root ,"Bool", true, "Example boolean", HypercubeRank.DEFAULT);
        numberSetting(root, "Number", 1.23, "Example number", HypercubeRank.DEFAULT);
        enumSetting(root, "Enum", "Good", "Example enum", HypercubeRank.DEFAULT, "Good", "Bad");
         */

        enumSetting(root, "Queue Messages", "Main Chat", "Join & Leave Queue Messages", HypercubeRank.JRHELPER, "Main Chat", "Side Chat", "Toast", "Hidden");

        return root;
    }

    private static void stringSetting(JsonObject obj, String name, String value, String desc, HypercubeRank rank) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static <T extends JsonElement> void objectSetting(JsonObject obj, String name, T json, String desc, HypercubeRank rank) {
        obj.add(name, json);
        obj.addProperty("desc:" + name, desc);
    }

    private static void booleanSetting(JsonObject obj, String name, boolean value, String desc, HypercubeRank rank) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static void numberSetting(JsonObject obj, String name, double value, String desc, HypercubeRank rank) {
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
    }

    private static void enumSetting(JsonObject obj, String name, String value, String desc, HypercubeRank rank, String... values) {
        JsonArray array = new JsonArray();
        for(String s : values) {
            array.add(s);
        }
        obj.addProperty(name, value);
        obj.addProperty("desc:" + name, desc);
        obj.add("options:" + name, array);
    }
}
