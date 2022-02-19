package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.script.ScriptManager;

public class ScriptCreationScreen extends CScreen {

    protected ScriptCreationScreen() {
        super(100, 60);

        CTextField name = new CTextField("My Script", 2, 2, 96, 36, true);
        widgets.add(name);

        widgets.add(new CButton(2, 42, 48, 15, "Create", () -> {
            ScriptManager.getInstance().createScript(name.getText());
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));
        widgets.add(new CButton(50, 42, 48, 15, "Cancel", () -> {
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));
    }

    @Override
    public void onClose() {
        CodeUtilities.MC.setScreen(new ScriptListScreen());
    }
}
