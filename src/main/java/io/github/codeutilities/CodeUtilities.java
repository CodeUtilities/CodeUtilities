package io.github.codeutilities;

import io.github.codeutilities.commands.CommandManager;
import io.github.codeutilities.features.KeybindManager;
import io.github.codeutilities.features.PrivateMessageManipulator;
import io.github.codeutilities.features.SupportMessages;
import io.github.codeutilities.features.afk.AfkFeature;
import io.github.codeutilities.loader.Loader;
import io.github.codeutilities.script.ScriptManager;
import io.github.codeutilities.util.Scheduler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeUtilities implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final MinecraftClient MC = MinecraftClient.getInstance();

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

        LOGGER.info("Initialized");
    }
}
