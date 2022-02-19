package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import io.github.codeutilities.script.ScriptPart;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.event.ScriptEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScriptEditScreen extends CScreen {

    private final Script script;

    public ScriptEditScreen(Script script) {
        super(95, 100);
        this.script = script;
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

        ItemStack argumentsIcon = new ItemStack(Items.CHEST);
        argumentsIcon.setHoverName(new TextComponent("Arguments"));

        int y = 5;
        int index = 0;
        int indent = 0;
        for (ScriptPart part : script.getParts()) {
            if (part instanceof ScriptEvent se) {
                panel.add(new CItem(5, y, se.getType().getIcon()));
                panel.add(new CText(15, y + 3, new TextComponent(se.getType().getName())));
                indent = 5;
            } else if (part instanceof ScriptAction sa) {
                if (sa.getType() == ScriptActionType.CLOSE_BRACKET) {
                    indent -= 5;
                }

                panel.add(new CItem(5 + indent, y, sa.getType().getIcon()));
                panel.add(new CText(15 + indent, y + 3, new TextComponent(sa.getType().getName())));

                if (sa.getType().hasChildren()) {
                    indent += 5;
                }

                if (sa.getType() != ScriptActionType.CLOSE_BRACKET) {
                    CItem arguments = new CItem(65, y, argumentsIcon);
                    arguments.setClickListener((btn) ->
                        CodeUtilities.MC.setScreen(new ScriptEditActionScreen(sa, script))
                    );
                    panel.add(arguments);
                }
            } else {
                throw new IllegalArgumentException("Unknown script part type");
            }
            CItem add = new CItem(75, y - 5, addIcon);
            int currentIndex = index;
            add.setClickListener((btn) ->
                CodeUtilities.MC.setScreen(new ScriptAddActionScreen(script, currentIndex))
            );
            panel.add(add);

            CItem delete = new CItem(85, y, deleteIcon);
            delete.setClickListener((btn) -> {
                script.getParts().remove(currentIndex);
                CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
            });
            panel.add(delete);

            y += 10;
            index++;
        }

        CItem add = new CItem(75, y - 5, addIcon);
        add.setClickListener((btn) ->
            CodeUtilities.MC.setScreen(new ScriptAddActionScreen(script, script.getParts().size()))
        );
        panel.add(add);
    }

    @Override
    public void onClose() {
        ScriptManager.getInstance().saveScript(script);
        CodeUtilities.MC.setScreen(new ScriptListScreen());
    }
}
