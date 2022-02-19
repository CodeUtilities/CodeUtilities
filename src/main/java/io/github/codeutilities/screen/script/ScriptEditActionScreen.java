package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.argument.ScriptClientValueArgument;
import io.github.codeutilities.script.argument.ScriptNumberArgument;
import io.github.codeutilities.script.argument.ScriptTextArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScriptEditActionScreen extends CScreen {

    private final Script script;

    public ScriptEditActionScreen(ScriptAction action, Script script) {
        super(90, 100);
        this.script = script;

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

        int y = 5;
        int index = 0;
        for (ScriptArgument arg : action.getArguments()) {
            ItemStack icon;
            String text;
            if (arg instanceof ScriptTextArgument ta) {
                icon = new ItemStack(Items.BOOK);
                text = ta.value();
            } else if (arg instanceof ScriptNumberArgument na) {
                icon = new ItemStack(Items.SLIME_BALL);
                if (na.value() % 1 == 0) {
                    text = String.valueOf((int) na.value());
                } else {
                    text = String.valueOf(na.value());
                }
            } else if (arg instanceof ScriptVariableArgument va) {
                icon = new ItemStack(Items.MAGMA_CREAM);
                text = va.name();
            } else if (arg instanceof ScriptClientValueArgument cva) {
                icon = new ItemStack(Items.NAME_TAG);
                text = cva.getName();
            } else {
                throw new IllegalArgumentException("Invalid argument type");
            }
            widgets.add(new CItem(5, y, icon));
            widgets.add(new CText(15, y + 2, new TextComponent(text)));

            CItem delete = new CItem(80, y, deleteIcon);
            int currentIndex = index;
            delete.setClickListener((btn) -> {
                action.getArguments().remove(currentIndex);
                CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
            });
            widgets.add(delete);

            if (index != action.getArguments().size() - 1) {
                CItem add = new CItem(70, y-5, addIcon);
                add.setClickListener((btn) ->
                    CodeUtilities.MC.setScreen(new ScriptAddArgumentScreen(script, action, currentIndex))
                );
                widgets.add(add);
            }

            y += 10;
            index++;
        }

        CItem add = new CItem(5, y, addIcon);
        add.setClickListener((btn) ->
            CodeUtilities.MC.setScreen(new ScriptAddArgumentScreen(script, action, action.getArguments().size()))
        );
        widgets.add(add);
    }

    @Override
    public void onClose() {
        CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
    }
}
