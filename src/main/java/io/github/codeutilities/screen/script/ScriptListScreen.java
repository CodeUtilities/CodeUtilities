package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class ScriptListScreen extends CScreen {

    public ScriptListScreen() {
        super(90, 100);
        CScrollPanel panel = new CScrollPanel(0, 0, 120, 100);
        widgets.add(panel);


        ItemStack deleteIcon = new ItemStack(Items.RED_DYE);
        deleteIcon.setCustomName(new LiteralText("Delete")
            .fillStyle(Style.EMPTY
                .withColor(Formatting.RED)
                .withItalic(false)));

        ItemStack addIcon = new ItemStack(Items.LIME_DYE);
        addIcon.setCustomName(new LiteralText("Add")
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GREEN)
                .withItalic(false)));

        ItemStack editIcon = new ItemStack(Items.IRON_INGOT);
        editIcon.setCustomName(new LiteralText("Edit")
            .fillStyle(Style.EMPTY.withItalic(false)));

        ItemStack disableIcon = new ItemStack(Items.GRAY_DYE);
        disableIcon.setCustomName(new LiteralText("Disable")
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GRAY)
                .withItalic(false)));

        ItemStack enableIcon = new ItemStack(Items.LIGHT_GRAY_DYE);
        enableIcon.setCustomName(new LiteralText("Enable")
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GRAY)
                .withItalic(false)));


        int y = 5;
        for (Script s : ScriptManager.getInstance().getScripts()) {
            panel.add(new CText(5, y + 5, new LiteralText(s.getName())));

            CItem editButton = new CItem(60, y + 3, editIcon);
            panel.add(editButton);
            editButton.setClickListener((btn) -> {
                CodeUtilities.MC.setScreen(new ScriptEditScreen(s));
            });
            CItem deleteButton = new CItem(70, y + 3, deleteIcon);
            deleteButton.setClickListener((btn) -> {
                CodeUtilities.MC.setScreen(new ScriptDeletionScreen(s));
            });
            panel.add(deleteButton);

            if (s.disabled()) {
                CItem enableButton = new CItem(80, y + 3, enableIcon);
                enableButton.setClickListener((btn) -> {
                    s.setDisabled(false);
                    ScriptManager.getInstance().saveScript(s);
                    CodeUtilities.MC.setScreen(new ScriptListScreen());
                });
                panel.add(enableButton);
            } else {
                CItem disableButton = new CItem(80, y + 3, disableIcon);
                disableButton.setClickListener((btn) -> {
                    s.setDisabled(true);
                    ScriptManager.getInstance().saveScript(s);
                    CodeUtilities.MC.setScreen(new ScriptListScreen());
                });
                panel.add(disableButton);
            }

            y += 12;
        }

        CItem addButton = new CItem(5, y + 3, addIcon);
        addButton.setClickListener((btn) -> {
            CodeUtilities.MC.setScreen(new ScriptCreationScreen());
        });
        panel.add(addButton);
    }
}
