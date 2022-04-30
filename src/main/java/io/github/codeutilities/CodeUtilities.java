package io.github.codeutilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.github.codeutilities.commands.CommandManager;
import io.github.codeutilities.config.internal.ConfigFile;
import io.github.codeutilities.config.internal.ConfigInstruction;
import io.github.codeutilities.config.internal.gson.ConfigSerializer;
import io.github.codeutilities.config.internal.gson.types.*;
import io.github.codeutilities.config.internal.gson.types.list.*;
import io.github.codeutilities.config.structure.ConfigManager;
import io.github.codeutilities.config.types.*;
import io.github.codeutilities.config.types.list.*;
import io.github.codeutilities.features.KeybindManager;
import io.github.codeutilities.features.PrivateMessageManipulator;
import io.github.codeutilities.features.SupportMessages;
import io.github.codeutilities.features.afk.AfkFeature;
import io.github.codeutilities.loader.Loader;
import io.github.codeutilities.loader.v2.CodeInitializer;
import io.github.codeutilities.script.ScriptManager;
import io.github.codeutilities.util.Scheduler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeUtilities implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final MinecraftClient MC = MinecraftClient.getInstance();
    public static final String MOD_NAME = "CodeUtilities";

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConfigInstruction.class, new ConfigSerializer())
            .registerTypeAdapter(BooleanSetting.class, new BooleanSerializer())
            .registerTypeAdapter(IntegerSetting.class, new IntegerSerializer())
            .registerTypeAdapter(DoubleSetting.class, new DoubleSerializer())
            .registerTypeAdapter(FloatSetting.class, new FloatSerializer())
            .registerTypeAdapter(LongSetting.class, new LongSerializer())
            .registerTypeAdapter(StringSetting.class, new StringSerializer())
            .registerTypeAdapter(StringListSetting.class, new StringListSerializer())
            .registerTypeAdapter(EnumSetting.class, new EnumSerializer())
            .registerTypeAdapter(DynamicStringSetting.class, new DynamicStringSerializer())
            .registerTypeAdapter(SoundSetting.class, new SoundSerializer())
            .setPrettyPrinting()
            .create();
    public static final JsonParser JSON_PARSER = new JsonParser();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing");

        Loader loader = Loader.getInstance();
        loader.load(new CommandManager());
        loader.load(new ScriptManager());
        loader.load(new SupportMessages());
        loader.load(new AfkFeature());
        loader.load(new PrivateMessageManipulator());
        loader.load(new Scheduler());
        loader.load(new KeybindManager());

        CodeInitializer initializer = new CodeInitializer();
        initializer.add(new ConfigFile());
        initializer.add(new ConfigManager());

        LOGGER.info("Initialized");
    }
}
