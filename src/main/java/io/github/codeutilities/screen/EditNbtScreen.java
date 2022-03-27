package io.github.codeutilities.screen;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.util.chat.ChatUtil;
import net.minecraft.item.ItemStack;

public class EditNbtScreen extends CScreen {

    private final ItemStack item;

    public EditNbtScreen(ItemStack item) {
        super(120, 100);
        this.item = item;
        String nbt = "{}";
        try {
            CompoundTag tag = item.getTag();
            if (tag != null) {
                nbt = new TextComponentTagVisitor("  ", 0).visit(tag).getString();
            }
        } catch (Exception ignored) {
        }

        CTextField textField = new CTextField(nbt, 2, 2, 116, 96, true);

        textField.setChangedListener(() -> {
            try {
                CompoundTag tag = TagParser.parseTag(textField.getText());
                item.setTag(tag);
                textField.textColor = 0xFFFFFFFF;
            } catch (Exception err) {
                textField.textColor = 0xFFFF3333;
            }
        });
        widgets.add(textField);
    }

    @Override
    public void onClose() {
        if (CodeUtilities.MC.player.isCreative()) {
            CodeUtilities.MC.gameMode.handleCreativeModeItemAdd(item, CodeUtilities.MC.player.getInventory().selected + 36);
        } else {
            ChatUtil.sendMessage("Unable to edit item NBT (Not in creative mode)");
        }
        super.onClose();
    }
}
