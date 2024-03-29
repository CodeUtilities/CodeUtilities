package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.ConfigSounds;
import io.github.codeutilities.config.internal.QueueMessages;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.*;
import io.github.codeutilities.features.sidedchat.ChatRule;

public class SupportGroup extends ConfigGroup {
    public SupportGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        this.register(new EnumSetting<>("support_queuemessages", QueueMessages.class, QueueMessages.MAIN_CHAT));

        ChatRule chatRule = ChatRule.getChatRule(ChatRule.ChatRuleType.SUPPORT);
        ConfigSubGroup subGroup = new ConfigSubGroup(chatRule.getInternalName());
        subGroup.register(new EnumSetting<>(ChatRule.getChatRuleConfigSideName(chatRule), ChatRule.ChatSide.class, ChatRule.ChatSide.MAIN));
        subGroup.register(new SoundSetting(ChatRule.getChatRuleConfigSoundName(chatRule)).setSelected(ConfigSounds.NONE));
        this.register(subGroup);
    }
}