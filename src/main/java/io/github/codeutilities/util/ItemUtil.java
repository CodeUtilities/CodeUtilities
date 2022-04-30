package io.github.codeutilities.util;

import java.util.ArrayList;
import java.util.List;

import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class ItemUtil {

    public static void giveItem(ItemStack item) {
        MinecraftClient mc = CodeUtilities.MC;
        DefaultedList<ItemStack> mainInventory = mc.player.getInventory().main;

        for (int index = 0; index < mainInventory.size(); index++) {
            ItemStack i = mainInventory.get(index);
            ItemStack compareItem = i.copy();
            compareItem.setCount(item.getCount());
            if (item == compareItem) {
                while (i.getCount() < i.getMaxCount() && item.getCount() > 0) {
                    i.setCount(i.getCount() + 1);
                    item.setCount(item.getCount() - 1);
                }
            } else {
                if (i.getItem() == Items.AIR) {
                    if (index < 9)
                        MinecraftClient.getInstance().interactionManager.clickCreativeStack(item, index + 36);
                    mainInventory.set(index, item);
                    return;
                }
            }
        }
    }

    public static boolean handEmpty() {
        return MinecraftClient.getInstance().player.getMainHandStack().isEmpty();
    }

    public static void setHandItem(ItemStack item) {
        MinecraftClient mc = CodeUtilities.MC;
        mc.interactionManager.clickCreativeStack(item, mc.player.getInventory().selectedSlot + 36);
    }
    
    public static List<ItemStack> fromListTag(NbtList listTag) {
        List<ItemStack> stacks = new ArrayList<>();
        for (NbtElement tag : listTag) {
            stacks.add(ItemStack.fromNbt((NbtCompound) tag));
        }
        return stacks;
    }

	public static void giveCreativeItem(ItemStack item, boolean preferHand) {
        MinecraftClient mc = CodeUtilities.MC;
        DefaultedList<ItemStack> mainInventory = mc.player.getInventory().main;

        if (preferHand) {
            if (mc.player.getMainHandStack().isEmpty()) {
                mc.interactionManager.clickCreativeStack(item, mc.player.getInventory().selectedSlot + 36);
                return;
            }
        }

        for (int index = 0; index < mainInventory.size(); index++) {
            ItemStack i = mainInventory.get(index);
            ItemStack compareItem = i.copy();
            compareItem.setCount(item.getCount());
            if (item == compareItem) {
                while (i.getCount() < i.getMaxCount() && item.getCount() > 0) {
                    i.setCount(i.getCount() + 1);
                    item.setCount(item.getCount() - 1);
                }
            } else {
                if (i.getItem() == Items.AIR) {
                    if (index < 9)
                        mc.interactionManager.clickCreativeStack(item, index + 36);
                    mainInventory.set(index, item);
                    return;
                	}
            	}
        	}
    	}

	public static List<ItemStack> fromItemContainer(ItemStack container) {
        NbtList nbt = container.getOrCreateNbt().getCompound("BlockEntityTag").getList("Items", 10);
        return fromListTag(nbt);
    }
	
	}
