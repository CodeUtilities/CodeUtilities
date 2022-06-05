package io.github.codeutilities.commands.item;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.screen.commands.templates.TemplatesScreen;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.TranslatableText;

public class TemplatesCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("templates")
                .executes(ctx -> {
                    if (CodeUtilities.MC.player.isCreative()) {
                        TemplatesScreen templateStorageUI = new TemplatesScreen();
                        CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(templateStorageUI));
                    } else {
                        ChatUtil.sendMessage(new TranslatableText("command.codeutilities.require_dev_mode", ChatType.FAIL));
                    }
                    return 1;
                })
        );
    }

    @Override
    public String getDescription() {
        return "[blue]/templates[reset]\n"
                + "\n"
                + "Shows a list of recently used code templates. Click on the item to get it in your inventory.";
    }

    @Override
    public String getName() {
        return "/templates";
    }
}
