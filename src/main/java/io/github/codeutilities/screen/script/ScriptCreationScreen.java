package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.script.ScriptManager;
import net.minecraft.sound.SoundEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptCreationScreen extends CScreen {

    // invalid file name chars
    // there's definitely a better place to put this but this felt the simplest rn
    Pattern ILLEGAL_CHARS = Pattern.compile("[\\\\/:*?\"<>|]");

    protected ScriptCreationScreen() {
        super(100, 60);

        CTextField name = new CTextField("My Script", 2, 2, 96, 36, true);

        name.setChangedListener(() -> name.textColor = 0xFFFFFF);

        widgets.add(name);

        widgets.add(new CButton(2, 42, 48, 15, "Create", () -> {
            String scriptName = name.getText();

            Matcher m = ILLEGAL_CHARS.matcher(scriptName);

            if (m.find()) {
                name.textColor = 0xFF3333;
                return;
            }

            ScriptManager.getInstance().createScript(name.getText());
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));
        widgets.add(new CButton(50, 42, 48, 15, "Cancel", () -> {
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new ScriptListScreen());
    }
}
