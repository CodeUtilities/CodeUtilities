package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.commands.codevault.CodeVaultScreen;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class CodeVaultCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        MinecraftClient mc = CodeUtilities.MC;
        cd.register(literal("codevault")
            .executes(ctx -> {
                if (mc.player.isCreative()) {
                    CodeVaultScreen menu = new CodeVaultScreen();
                    CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(menu));
                }
                return 1;
            })
        );
    }

    @Override
    public String getDescription() {
        return "[blue]/codevault[reset]\n"
                + "\n"
                + "Browses the code templates uploaded by other people. Click the item in the menu to get the code template.\n"
                + "To add your own code templates, join the plot [green]Code Vault[reset] (ID:43780) and upload the templates there.";
    }

    @Override
    public String getName() {
        return "/codevault";
    }
}
