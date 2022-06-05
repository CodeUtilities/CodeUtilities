package io.github.codeutilities.commands.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TitleCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	MinecraftClient mc = CodeUtilities.MC;
        reg("previewtitle",mc,cd);
        reg("titlepreview",mc,cd);
    }

    @Override
    public String getDescription() {
        return "[blue]/previewtitle [text][reset]\n"
                + "[blue]/titlepreview [text][reset]\n"
                + "\n"
                + "Previews the title text. If no text is specified, the name of the item you are holding will show up.";
    }

    @Override
    public String getName() {
        return "/previewtitle";
    }

    public void reg(String name, MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal(name)
            .then(argument("message", StringArgumentType.greedyString())
                .executes(ctx -> {
                    Text msg = new LiteralText(
                        ctx.getArgument("message", String.class)
                            .replace("&", "§"));

                    mc.inGameHud.setTitle(msg);
                    mc.inGameHud.setTitleTicks(20, 60, 20);
                    return 1;
                })
            )
            .executes(ctx -> {
                mc.inGameHud.setTitle(mc.player.getMainHandStack().getName());
                mc.inGameHud.setTitleTicks(20, 60, 20);
                return 1;
            })
        );
    }
}
