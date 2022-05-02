package io.github.codeutilities.config.impl;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.types.BooleanSetting;

public class MiscGroup extends ConfigGroup {
    public MiscGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        this.register(new BooleanSetting("loadTabStars", true));
        this.register(new BooleanSetting("relocateTabStars", false));

        this.register(new BooleanSetting("clickToReply", false));

        if (CodeUtilities.PLAYER_NAME.equals("TechStreet") ||
                CodeUtilities.PLAYER_NAME.equals("BlazeMCworld") ||
                CodeUtilities.PLAYER_NAME.equals("Reasonless") ||
                CodeUtilities.PLAYER_NAME.equals("KabanFriends") ||
                CodeUtilities.PLAYER_NAME.equals("tk2217") ||
                CodeUtilities.PLAYER_NAME.equals("Vattendroppen236")) {

            this.register(new BooleanSetting("debug", false));
        }
    }
}