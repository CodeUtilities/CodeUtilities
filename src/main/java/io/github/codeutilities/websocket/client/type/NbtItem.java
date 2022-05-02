package io.github.codeutilities.websocket.client.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

import java.io.IOException;

public class NbtItem extends SocketItem {

    @Override
    public String getIdentifier() {
        return "nbt";
    }

    @Override
    public ItemStack getItem(String data) throws Exception {
        ItemStack stack;
        try {
            NbtCompound nbt = StringNbtReader.parse(data);
            stack = ItemStack.fromNbt(nbt);
        } catch (RuntimeException | CommandSyntaxException e) {
            throw new IOException("Failed to parse provided NBT data.");
        }

        return stack;
    }
}