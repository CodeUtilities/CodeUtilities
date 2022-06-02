package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.ItemUtil;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;

public class DfGiveCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("dfgive")
                .then(
                    argument("item", ItemStackArgumentType.itemStack())
                        .then(
                            argument("amount", IntegerArgumentType.integer(1,64))
                                .executes(ctx -> {
                                    give(
                                        ItemStackArgumentType.getItemStackArgument(ctx, "item"),
                                        IntegerArgumentType.getInteger(ctx, "amount")
                                    );
                                    return 1;
                                })
                        )
                        .executes(ctx -> {
                            give(ItemStackArgumentType.getItemStackArgument(ctx, "item"),1);
                            return 1;
                        })

                ).then(literal("clipboard").executes(ctx -> {
                        String clipboard;
                        try {
                            clipboard = CodeUtilities.MC.keyboard.getClipboard();
                        }
                        catch (Exception e){
                            ChatUtil.error("Unable to get the clipboard.");
                            return -1;
                        }

                        clipboard = clipboard.replaceFirst("^/?(df)?give ((\\w{3,16})|(@[aeprs](\\[.]?)?) )?","");
                        ChatUtil.executeCommand("/dfgive " + clipboard);

                        return 1;
                    }))
        );
    }

    private void give(ItemStackArgument input, int amount) throws CommandSyntaxException {
        if (amount > input.getItem().getMaxCount()) {
            ChatUtil.error("This item only supports a max stack size of " + input.getItem().getMaxCount());
            return;
        }
        if (amount < 1) {
            ChatUtil.error("The minimum amount of items to give is 1");
        }
        if (!CodeUtilities.MC.player.isCreative()) {
            ChatUtil.error("You must be in creative mode to use this command");
            return;
        }
        ItemStack stack = input.createStack(amount,false);

        if (ItemUtil.handEmpty()) {
            ItemUtil.setHandItem(stack);
        } else {
            ItemUtil.giveItem(stack);
        }
    }
}
