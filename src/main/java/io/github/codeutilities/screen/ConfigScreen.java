package io.github.codeutilities.screen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import io.github.codeutilities.config.ConfigManager;
import io.github.codeutilities.screen.widget.CTextField;
import java.io.StringWriter;

public class ConfigScreen extends CScreen {

    private final CTextField data;

    public ConfigScreen() {
        super(120, 100);

        StringWriter sWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(sWriter);
        writer.setIndent("  ");
        writer.setLenient(true);

        try {
            Streams.write(ConfigManager.getConfig().json(), writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String src = sWriter.toString();

        data = new CTextField(src, 0, 0, 120, 100, true);
        widgets.add(data);
    }

    @Override
    public void close() {
        JsonObject newJson = JsonParser.parseString(data.getText()).getAsJsonObject();

        for (String key : newJson.keySet()) {
            ConfigManager.getConfig().json().add(key, newJson.get(key));
        }

        super.close();
    }
}
