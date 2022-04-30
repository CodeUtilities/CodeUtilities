package io.github.codeutilities.commands.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class SubTitleCommand implements Command {

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	MinecraftClient mc = CodeUtilities.MC;
        reg("previewsubtitle",mc,cd);
        reg("subtitlepreview",mc,cd);
    }
    
    /*

    @Override
    public String getDescription() {
        return "[blue]/previewsubtitle [text][reset]\n"
                + "[blue]/subtitlepreview [text][reset]\n"
                + "\n"
                + "Previews the sub-title text. If no text is specified, the name of the item you are holding will show up.";
    }

    @Override
    public String getName() {
        return "/previewsubtitle";
    }
    
    */

    public void reg(String name, MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal(name)
            .then(argument("message", StringArgumentType.greedyString())
                .executes(ctx -> {
                    Text msg = new LiteralText(
                        ctx.getArgument("message", String.class)
                            .replace("&", "§"));

                    mc.inGameHud.setTitle(new LiteralText("§c"));
                    mc.inGameHud.setSubtitle(msg);
                    mc.inGameHud.setTitleTicks(20, 60, 20);
                    return 1;
                })
            )
            .executes(ctx -> {
                mc.inGameHud.setTitle(new LiteralText("§c"));
                mc.inGameHud.setSubtitle(mc.player.getMainHandStack().getName());
                mc.inGameHud.setTitleTicks(20, 60, 20);
                
                return 1;
            })
        );
    }
}
