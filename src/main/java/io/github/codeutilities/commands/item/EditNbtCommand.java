package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.EditNbtScreen;
import io.github.codeutilities.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class EditNbtCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("editnbt")
                .executes(ctx -> {
                    Minecraft mc = CodeUtilities.MC;

                    ItemStack stack = mc.player.getMainHandItem();

                    if (stack.isEmpty()) {
                        ChatUtil.error("You must be holding an item to edit its NBT data.");
                        return 1;
                    }
                    if (!mc.player.isCreative()) {
                        ChatUtil.error("You must be in creative mode to edit an item's NBT data.");
                        return 1;
                    }

                    mc.tell(() -> mc.setScreen(new EditNbtScreen(stack)));

                    return 1;
                })
        );
    }
}
