package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.ConfigSounds;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.EnumSetting;
import io.github.codeutilities.config.types.SoundSetting;
import io.github.codeutilities.features.sidedchat.ChatRule;

public class AdminGroup extends ConfigGroup {
    public AdminGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        ChatRule chatRule = ChatRule.getChatRule(ChatRule.ChatRuleType.ADMIN);
        ConfigSubGroup subGroup = new ConfigSubGroup(chatRule.getInternalName());
        subGroup.register(new EnumSetting<>(ChatRule.getChatRuleConfigSideName(chatRule), ChatRule.ChatSide.class, ChatRule.ChatSide.MAIN));
        subGroup.register(new SoundSetting(ChatRule.getChatRuleConfigSoundName(chatRule)).setSelected(ConfigSounds.NONE));
        this.register(subGroup);
    }
}