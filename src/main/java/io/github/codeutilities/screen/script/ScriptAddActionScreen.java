package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionCategory;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.event.ScriptEvent;
import io.github.codeutilities.script.event.ScriptEventType;
import java.util.ArrayList;
import java.util.List;

public class ScriptAddActionScreen extends CScreen {

    private final Script script;
    private final int insertIndex;

    public ScriptAddActionScreen(Script script, int insertIndex, ScriptActionCategory category) {
        super(size(category), size(category));
        int size = size(category);
        this.script = script;
        this.insertIndex = insertIndex;

        int x = 3;
        int y = 3;

        if (category == null) {
            for (ScriptEventType type : ScriptEventType.values()) {
                CItem item = new CItem(x, y, type.getIcon());
                item.setClickListener((btn) -> {
                    ScriptEvent event = new ScriptEvent(type);
                    script.getParts().add(insertIndex, event);
                    CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
                });
                widgets.add(item);
                x += 10;
                if (x >= size-10) {
                    x = 3;
                    y += 10;
                }
            }
        }

        for (ScriptActionType type : ScriptActionType.values()) {
            if (type.getCategory() != category) continue;

            CItem item = new CItem(x, y, type.getIcon());
            item.setClickListener((btn) -> {
                ScriptAction action = new ScriptAction(type, new ArrayList<>());
                script.getParts().add(insertIndex, action);
                if (action.getType().hasChildren()) {
                    script.getParts().add(insertIndex + 1, new ScriptAction(ScriptActionType.CLOSE_BRACKET, List.of()));
                }
                CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
            });
            widgets.add(item);
            x += 10;
            if (x >= size-10) {
                x = 3;
                y += 10;
            }
        }
    }

    private static int size(ScriptActionCategory category) {
        int amount = 0;
        if (category == null) {
            amount = ScriptEventType.values().length;
        } else {
            for (ScriptActionType type : ScriptActionType.values()) {
                if (type.getCategory() == category) {
                    amount++;
                }
            }
        }
        return (int) (Math.ceil(Math.sqrt(amount))*10)+4;
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, insertIndex));
    }
}
