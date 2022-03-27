package io.github.codeutilities.screen;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.util.chat.ChatUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.visitor.NbtTextFormatter;

public class EditNbtScreen extends CScreen {

    private final ItemStack item;

    public EditNbtScreen(ItemStack item) {
        super(120, 100);
        this.item = item;
        String nbt = "{}";
        try {
            NbtCompound tag = item.getNbt();
            if (tag != null) {
                nbt = new NbtTextFormatter("  ", 0).apply(tag).getString();
            }
        } catch (Exception ignored) {
        }

        CTextField textField = new CTextField(nbt, 2, 2, 116, 96, true);

        textField.setChangedListener(() -> {
            try {
                NbtCompound tag = StringNbtReader.parse(textField.getText());
                item.setNbt(tag);
                textField.textColor = 0xFFFFFFFF;
            } catch (Exception err) {
                textField.textColor = 0xFFFF3333;
            }
        });
        widgets.add(textField);
    }

    @Override
    public void close() {
        if (CodeUtilities.MC.player.isCreative()) {
            CodeUtilities.MC.interactionManager.clickCreativeStack(item, CodeUtilities.MC.player.getInventory().selectedSlot + 36);
        } else {
            ChatUtil.sendMessage("Unable to edit item NBT (Not in creative mode)");
        }
        super.close();
    }
}
