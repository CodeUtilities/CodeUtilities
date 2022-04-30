package io.github.codeutilities.commands.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class CopyTextCommand implements Command {

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("copytxt")
                .then(argument("text", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            CodeUtilities.MC.keyboard.setClipboard(ctx.getArgument("text", String.class));
                            ChatUtil.sendMessage("Copied text!", ChatType.INFO_BLUE);
                            return 1;
                        })
                )
        );
    }
    
    /*
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
    */

}
