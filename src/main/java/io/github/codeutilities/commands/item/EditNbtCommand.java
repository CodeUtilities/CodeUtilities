package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.EditNbtScreen;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class EditNbtCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("editnbt")
                .executes(ctx -> {
                    MinecraftClient mc = CodeUtilities.MC;

                    ItemStack stack = mc.player.getActiveItem();

                    if (stack.isEmpty()) {
                        ChatUtil.sendMessage("You must be holding an item to edit its NBT data.", ChatType.FAIL);
                        return 1;
                    }
                    if (!mc.player.isCreative()) {
                        ChatUtil.sendMessage("You must be in creative mode to edit an item's NBT data.", ChatType.FAIL);
                        return 1;
                    }

                    mc.setScreen(new EditNbtScreen(stack));

                    return 1;
                })
        );
    }

    @Override
    public String getDescription() {
        return "[blue]/editnbt[reset]\n"
                + "\n"
                + "Opens a menu to edit item data.";
    }

    @Override
    public String getName() {
        return "/edititem";
    }
}
