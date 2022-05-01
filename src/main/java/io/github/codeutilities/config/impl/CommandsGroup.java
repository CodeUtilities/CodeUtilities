package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.ConfigSounds;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.*;

public class CommandsGroup extends ConfigGroup {
    public CommandsGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        // Afk Feature
        ConfigSubGroup autoMessage = new ConfigSubGroup("autoafk");
        autoMessage.register(new BooleanSetting("autoafk", true));
        autoMessage.register(new IntegerSetting("autoafk_time", 2400));
        autoMessage.register(new StringSetting("autoafk_response", "I am currently AFK (Automated Message)"));
        autoMessage.register(new SoundSetting("autoafk_sound").setSelected(ConfigSounds.SHIELD_BLOCK));

        this.register(autoMessage);
    }
}