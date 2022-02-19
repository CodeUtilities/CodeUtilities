package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;

public class ScriptDeletionScreen extends CScreen {

    public ScriptDeletionScreen(Script script) {
        super(100,50);

        widgets.add(new CButton(5,4,90,20,"Delete",() -> {
            ScriptManager.getInstance().deleteScript(script);
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));

        widgets.add(new CButton(5,28,90,20,"Abort",() -> {
            CodeUtilities.MC.setScreen(new ScriptListScreen());
        }));
    }

    @Override
    public void onClose() {
        CodeUtilities.MC.setScreen(new ScriptListScreen());
    }
}
