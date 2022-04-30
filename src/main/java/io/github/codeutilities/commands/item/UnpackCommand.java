package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.ItemUtil;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class UnpackCommand implements Command {

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	MinecraftClient mc = CodeUtilities.MC;
        cd.register(literal("unpack")
                .executes(ctx -> {
                    if (mc.player.isCreative()) {
                        ItemStack handItem = mc.player.getMainHandStack();
                        if (!handItem.getOrCreateNbt().getCompound("BlockEntityTag").isEmpty()) {

                            int items = 0;
                            for (ItemStack stack : ItemUtil.fromItemContainer(handItem)) {
                                if (!stack.isEmpty()) {
                                    items++;
                                    ItemUtil.giveCreativeItem(stack, true);
                                }
                            }

                            if (items == 0) {
                                ChatUtil.sendMessage("There are no items stored in this container!", ChatType.FAIL);
                            } else {
                                if (items == 1) {
                                    ChatUtil.sendMessage("Unpacked §b" + items + "§r item!", ChatType.SUCCESS);
                                } else {
                                    ChatUtil.sendMessage("Unpacked §b" + items + "§r items!", ChatType.SUCCESS);
                                }
                            }
                        } else {
                            ChatUtil.sendMessage("There are no items stored in this item!", ChatType.FAIL);
                        }
                    } else {
                        return -1;
                    }
                    return 1;
                })
        );
    }

    /*
    @Override
    public String getDescription() {
        return "[blue]/unpack[reset]\n\nExtracts the items in a container you are holding.";
    }

    @Override
    public String getName() {
        return "/unpack";
    }
    */
}
