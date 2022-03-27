package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.ItemUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.world.item.ItemStack;

public class DfGiveCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("dfgive")
                .then(
                    argument("item", ItemArgument.item())
                        .then(
                            argument("amount", IntegerArgumentType.integer(1,64))
                                .executes(ctx -> {
                                    give(
                                        ItemArgument.getItem(ctx, "item"),
                                        IntegerArgumentType.getInteger(ctx, "amount")
                                    );
                                    return 1;
                                })
                        )
                        .executes(ctx -> {
                            give(ItemArgument.getItem(ctx, "item"),1);
                            return 1;
                        })
                )
        );
    }

    private void give(ItemInput input, int amount) throws CommandSyntaxException {
        if (amount > input.getItem().getMaxStackSize()) {
            ChatUtil.error("This item only supports a max stack size of " + input.getItem().getMaxStackSize());
            return;
        }
        if (amount < 1) {
            ChatUtil.error("The minimum amount of items to give is 1");
        }
        if (!CodeUtilities.MC.player.isCreative()) {
            ChatUtil.error("You must be in creative mode to use this command");
            return;
        }
        ItemStack stack = input.createItemStack(amount,false);

        if (ItemUtil.handEmpty()) {
            ItemUtil.setHandItem(stack);
        } else {
            ItemUtil.giveItem(stack);
        }
    }
}
