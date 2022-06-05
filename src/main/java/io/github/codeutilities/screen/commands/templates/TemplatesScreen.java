package io.github.codeutilities.screen.commands.templates;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.features.commands.templates.TemplateItem;
import io.github.codeutilities.features.commands.templates.TemplateStorageHandler;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class TemplatesScreen extends CScreen {

    public TemplatesScreen() {
        super(70, 50);
        List<ItemStack> items = new ArrayList<>();

        for (TemplateItem item : TemplateStorageHandler.getInstance().getRegistered()) {
            items.add(item.getStack());
        }
        CScrollPanel panel = new CScrollPanel(0, 0, 70, 50);

        CText header = new CText(3, 3, new LiteralText("Recent Templates"), 0x333333, 0.8f, false, false);

        int x_offset = 8;
        int y_offset = 8;
        for (ItemStack stack : items) {
            CItem template = new CItem(x_offset, y_offset, stack);

            template.setClickListener((i) -> {
                ItemUtil.giveCreativeItem(stack, true);
                CodeUtilities.MC.player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 2, 1);
            });

            panel.add(template);

            x_offset += 8;

            if (x_offset == 64) {
                x_offset = 8;
                y_offset += 8;
            }

        }


        widgets.add(header);
        widgets.add(panel);
    }
}
