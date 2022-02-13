package io.github.codeutilities;

import io.github.codeutilities.commands.CommandManager;
import io.github.codeutilities.loader.Loader;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Codeutilities implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Minecraft MC = Minecraft.getInstance();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing");

        Loader loader = Loader.getInstance();

        loader.load(new CommandManager());

        LOGGER.info("Initialized");
    }
}
