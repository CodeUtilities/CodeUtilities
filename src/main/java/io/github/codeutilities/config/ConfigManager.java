package io.github.codeutilities.config;

import io.github.codeutilities.event.ShutdownEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;

public class ConfigManager implements Loadable {

    private static Config config;

    @Override
    public void load() {
        config = new Config();
        config.loadFromFile();
        config.merge(ConfigDefaults.getDefaults());

        EventManager.getInstance().register(ShutdownEvent.class, (e) -> {
            config.saveToFile();
        });
    }

    public static Config getConfig() {
        return config;
    }
}
