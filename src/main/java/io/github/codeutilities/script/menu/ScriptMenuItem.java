package io.github.codeutilities.script.menu;

import io.github.codeutilities.screen.widget.CItem;
import net.minecraft.item.ItemStack;

public class ScriptMenuItem extends CItem implements ScriptWidget {

    private final String identifier;
    public ScriptMenuItem(int x, int y, ItemStack item, String identifier) {
        super(x, y, item);
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
