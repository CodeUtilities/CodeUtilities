package io.github.codeutilities.commands.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ActionbarCommand implements Command {

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	MinecraftClient mc = CodeUtilities.MC;
        reg("previewactionbar",mc,cd);
        reg("actionbarpreview",mc,cd);
    }

    /*    
    @Override
    public String getDescription() {
        return "[blue]/previewactionbar [text][reset]\n"
            + "[blue]/actionbarpreview [text][reset]\n"
            + "\n"
            + "Previews the action bar text. If no text is specified, the name of the item you are holding will show up.";
    }

    @Override
    public String getName() {
        return "/previewactionbar";
    }
     */


    public void reg(String name, MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal(name)
            .then(argument("message", StringArgumentType.greedyString())
                .executes(ctx -> {
                    Text msg = new LiteralText(
                        ctx.getArgument("message", String.class)
                            .replace("&", "§"));

                    mc.player.sendMessage(msg, true);
                    return 1;
                })
            )
            .executes(ctx -> {
                mc.player.sendMessage(mc.player.getMainHandStack().getName(),true);
                return 1;
            })
        );
    }
}
