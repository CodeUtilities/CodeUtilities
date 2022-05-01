package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.ConfigSounds;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.*;
import io.github.codeutilities.features.sidedchat.ChatRule;

public class ScreenGroup extends ConfigGroup {
    public ScreenGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        this.register(new IntegerSetting("sidechat_width",0));
        this.register(new BooleanSetting("cpuOnScreen",true));

        for (ChatRule chatRule : ChatRule.getChatRules()) {
            if (chatRule.getChatRuleType().equals(ChatRule.ChatRuleType.MESSAGE) || chatRule.getChatRuleType().equals(ChatRule.ChatRuleType.CUSTOM)) {
                ConfigSubGroup subGroup = new ConfigSubGroup(chatRule.getInternalName());

                if (chatRule.getChatRuleType() == ChatRule.ChatRuleType.CUSTOM)
                    subGroup.register(new StringSetting("custom_filter"));

                subGroup.register(new EnumSetting<>(ChatRule.getChatRuleConfigSideName(chatRule), ChatRule.ChatSide.class, ChatRule.ChatSide.MAIN));
                subGroup.register(new SoundSetting(ChatRule.getChatRuleConfigSoundName(chatRule)).setSelected(ConfigSounds.NONE));

                this.register(subGroup);
            }
        }
    }
}