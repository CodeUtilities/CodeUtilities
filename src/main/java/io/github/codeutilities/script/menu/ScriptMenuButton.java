package io.github.codeutilities.script.menu;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.script.Script;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class ScriptMenuButton extends CButton implements ScriptWidget {

    private final String identifier;
    private final Script script;
    public ScriptMenuButton(int x, int y, int width, int height, String text, String identifier, Script script) {
        super(x, y, width, height, text, () -> {});
        this.identifier = identifier;
        this.script = script;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (getBounds().contains(x,y)) {
            CodeUtilities.MC.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1f,1f));
            script.invoke(new ScriptMenuClickButtonEvent(identifier));
            return true;
        }
        return false;
    }
}
