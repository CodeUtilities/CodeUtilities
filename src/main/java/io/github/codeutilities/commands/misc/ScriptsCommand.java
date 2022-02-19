package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.script.ScriptListScreen;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class ScriptsCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("scripts").executes(ctx -> {
                CodeUtilities.MC.tell(() -> CodeUtilities.MC.setScreen(new ScriptListScreen()));
                return 0;
            })
        );
    }
}
