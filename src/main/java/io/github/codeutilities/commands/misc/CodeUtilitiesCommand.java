package io.github.codeutilities.commands.misc;


import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.commands.codeutilities.CodeUtilitiesScreen;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.TranslatableText;

public class CodeUtilitiesCommand implements Command {
    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("codeutilities")
                .executes(ctx -> {
                    CodeUtilitiesScreen gui = new CodeUtilitiesScreen();
                    CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(gui));
                    return 1;
                })
        );
    }

    @Override
    public String getDescription() {
        return "[blue]/codeutilities[reset]\n\nShows information about this mod, such as this help menu, mod contributors, etc.";
    }

    @Override
    public String getName() {
        return "/codeutilities";
    }
}