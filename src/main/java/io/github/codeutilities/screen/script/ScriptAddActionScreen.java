package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.event.ScriptEvent;
import io.github.codeutilities.script.event.ScriptEventType;
import java.util.ArrayList;
import java.util.List;

public class ScriptAddActionScreen extends CScreen {

    private final Script script;
    private static final int WIDTH = 105;

    public ScriptAddActionScreen(Script script, int insertIndex) {
        super(WIDTH, 52);
        this.script = script;

        int x = 5;
        int y = 5;
        for (ScriptEventType type : ScriptEventType.values()) {
            CItem item = new CItem(x, y, type.getIcon());
            item.setClickListener((btn) -> {
                ScriptEvent event = new ScriptEvent(type);
                script.getParts().add(insertIndex, event);
                CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
            });
            widgets.add(item);
            x += 10;
            if (x > WIDTH-10) {
                x = 5;
                y += 10;
            }
        }
        for (ScriptActionType type : ScriptActionType.values()) {
            CItem item = new CItem(x, y, type.getIcon());
            item.setClickListener((btn) -> {
                ScriptAction action = new ScriptAction(type, new ArrayList<>());
                script.getParts().add(insertIndex, action);
                if (action.getType().hasChildren()) {
                    script.getParts().add(insertIndex+1, new ScriptAction(ScriptActionType.CLOSE_BRACKET, List.of()));
                }
                CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
            });
            widgets.add(item);
            x += 10;
            if (x > WIDTH-5) {
                x = 5;
                y += 10;
            }
        }
    }

    @Override
    public void onClose() {
        CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
    }
}
