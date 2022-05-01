package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.features.commands.afk.AfkFeature;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class AfkCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
                literal("afk").executes(ctx -> {
                    if (AfkFeature.afk) {
                        ChatUtil.sendMessage("You are no longer afk!", ChatType.SUCCESS);
                        for (HypercubePrivateMessage message : AfkFeature.afkMessages) {
                            ChatUtil.sendMessage(message.getText()
                                    .append(new LiteralText("(" + message.getDateFormat() + ")")
                                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
                        }

                        AfkFeature.afk = false;
                        AfkFeature.afkMessages.clear();
                    } else {
                        ChatUtil.sendMessage("You are now afk!", ChatType.SUCCESS);
                        AfkFeature.afk = true;
                        AfkFeature.afkMessages.clear();
                    }

                    return 0;
                })
        );
    }
}

