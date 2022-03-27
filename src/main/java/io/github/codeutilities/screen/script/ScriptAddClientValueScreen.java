package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.argument.ScriptClientValueArgument;

public class ScriptAddClientValueScreen extends CScreen {

    private final Script script;
    private final ScriptAction action;
    private final int insertIndex;
    private static final int WIDTH = 55;

    public ScriptAddClientValueScreen(ScriptAction action, Script script, int insertIndex) {
        super(WIDTH, 52);
        this.script = script;
        this.action = action;
        this.insertIndex = insertIndex;

        int x = 5;
        int y = 5;
        for (ScriptClientValueArgument arg : ScriptClientValueArgument.values()) {
            CItem item = new CItem(x, y, arg.getIcon());
            item.setClickListener((btn) -> {
                action.getArguments().add(insertIndex, arg);
                CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
            });
            widgets.add(item);
            x += 10;
            if (x > WIDTH-10) {
                x = 5;
                y += 10;
            }
        }
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new ScriptAddArgumentScreen(script, action, insertIndex));
    }
}
