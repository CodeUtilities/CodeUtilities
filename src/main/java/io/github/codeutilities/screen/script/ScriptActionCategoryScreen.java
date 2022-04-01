package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptActionCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;

public class ScriptActionCategoryScreen extends CScreen {

    private static final int size;

    static {
        size = (int) (Math.ceil(Math.sqrt(ScriptActionCategory.values().length + 1)) * 10)+4;
    }

    private final Script script;

    public ScriptActionCategoryScreen(Script script, int insertIndex) {
        super(size, size);
        this.script = script;

        ItemStack eventsItem = new ItemStack(Items.DIAMOND);
        eventsItem.setCustomName(new LiteralText("Events").fillStyle(Style.EMPTY.withItalic(false)));

        int x = 3;
        int y = 3;

        CItem item = new CItem(x, y, eventsItem);
        widgets.add(item);

        item.setClickListener(btn -> CodeUtilities.MC.setScreen(new ScriptAddActionScreen(script, insertIndex, null)));

        x += 10;

        for (ScriptActionCategory category : ScriptActionCategory.values()) {
            CItem actionItem = new CItem(x, y, category.getIcon());
            widgets.add(actionItem);

            actionItem.setClickListener(btn -> CodeUtilities.MC.setScreen(new ScriptAddActionScreen(script, insertIndex, category)));

            x += 10;
            if (x >= size - 10) {
                x = 3;
                y += 10;
            }
        }
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
    }
}
