package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.internal.QueueMessages;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.BooleanSetting;
import io.github.codeutilities.config.types.EnumSetting;
import io.github.codeutilities.config.types.IntegerSetting;
import io.github.codeutilities.config.types.StringSetting;

public class SupportGroup extends ConfigGroup {
    public SupportGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        this.register(new EnumSetting<>("support_queuemessages", QueueMessages.class, QueueMessages.MAIN_CHAT));
    }
}