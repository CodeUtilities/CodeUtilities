package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScriptListScreen extends CScreen {

    public ScriptListScreen() {
        super(90, 100);
        CScrollPanel panel = new CScrollPanel(0, 0, 120, 100);
        widgets.add(panel);


        ItemStack deleteIcon = new ItemStack(Items.RED_DYE);
        deleteIcon.setHoverName(new TextComponent("Delete")
            .withStyle(Style.EMPTY
                .withColor(ChatFormatting.RED)
                .withItalic(false)));

        ItemStack addIcon = new ItemStack(Items.LIME_DYE);
        addIcon.setHoverName(new TextComponent("Add")
            .withStyle(Style.EMPTY
                .withColor(ChatFormatting.GREEN)
                .withItalic(false)));

        ItemStack editIcon = new ItemStack(Items.IRON_INGOT);
        editIcon.setHoverName(new TextComponent("Edit")
            .withStyle(Style.EMPTY.withItalic(false)));

        int y = 5;
        for (Script s : ScriptManager.getInstance().getScripts()) {
            panel.add(new CText(5, y + 5, new TextComponent(s.getName())));

            CItem editButton = new CItem(70, y + 3, editIcon);
            panel.add(editButton);
            editButton.setClickListener((btn) -> {
                CodeUtilities.MC.setScreen(new ScriptEditScreen(s));
            });
            CItem deleteButton = new CItem(80, y + 3, deleteIcon);
            deleteButton.setClickListener((btn) -> {
                CodeUtilities.MC.setScreen(new ScriptDeletionScreen(s));
            });
            panel.add(deleteButton);

            y += 12;
        }

        CItem addButton = new CItem(5, y + 3, addIcon);
        addButton.setClickListener((btn) -> {
            CodeUtilities.MC.setScreen(new ScriptCreationScreen());
        });
        panel.add(addButton);
    }
}
