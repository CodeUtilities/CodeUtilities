package io.github.codeutilities.util;

import io.github.codeutilities.Codeutilities;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ItemUtil {

    public static void giveItem(ItemStack stack) {
        Minecraft mc = Codeutilities.MC;

        if (!mc.player.isCreative()) {
            return;
        }

        for (int slot = 0; slot < 9*4; slot++) {
            if (mc.player.getInventory().getItem(slot).isEmpty()) {
                mc.player.getInventory().setItem(slot, stack);
                mc.player.getInventory().setItem(slot,stack);
                if (slot < 9) {
                    mc.gameMode.handleCreativeModeItemAdd(stack, slot+36);
                }
                return;
            } else if (mc.player.getInventory().getItem(slot).getItem() == stack.getItem() && mc.player.getInventory().getItem(slot).getCount() < stack.getMaxStackSize()) {
                int current = mc.player.getInventory().getItem(slot).getCount();
                int addition = stack.getCount();
                int max = stack.getMaxStackSize();
                int change = Math.min(max - current, addition);
                mc.player.getInventory().getItem(slot).setCount(current + change);
                stack.setCount(addition - change);
                if (slot < 9) {
                    mc.gameMode.handleCreativeModeItemAdd(mc.player.getInventory().getItem(slot), slot+36);
                }
                if (stack.getCount() == 0) {
                    return;
                }
            }
        }

        mc.gameMode.handleCreativeModeItemDrop(stack);
    }

    public static boolean handEmpty() {
        return Minecraft.getInstance().player.getMainHandItem().isEmpty();
    }

    public static void setHandItem(ItemStack stack) {
        Minecraft mc = Codeutilities.MC;
        if (!mc.player.isCreative()) {
            return;
        }
        mc.gameMode.handleCreativeModeItemAdd(
            stack,
            mc.player.getInventory().selected
        );
        mc.player.getInventory().setItem(
            mc.player.getInventory().selected,
            stack
        );
    }

}
