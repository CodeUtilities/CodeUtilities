package io.github.codeutilities.config.internal.gson.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.config.types.SoundSetting;
import java.lang.reflect.Type;

public class SoundSerializer implements JsonSerializer<SoundSetting> {

    @Override
    public JsonElement serialize(SoundSetting src, Type typeOfSrc,
        JsonSerializationContext context) {
        return new JsonPrimitive(src.getSelected());
    }
}
