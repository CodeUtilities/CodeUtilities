package io.github.codeutilities.config.types;

import io.github.codeutilities.config.ConfigSounds;

public class SoundSetting extends DropdownSetting<ConfigSounds> {

    public SoundSetting(String key) {
        super(key, DropdownSetting.fromEnum(ConfigSounds.NONE));
    }
}
