package io.github.codeutilities.commands.item;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import io.github.codeutilities.util.template.TemplateUtils;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class WebviewCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
                literal("webview")
                        .executes(ctx -> {
                            if (CodeUtilities.MC.player != null) {
                                ItemStack stack = CodeUtilities.MC.player.getActiveItem();
                                JsonObject template;

                                try {
                                    template = TemplateUtils.fromItemStack(stack);
                                } catch (Exception e) {
                                    ChatUtil.sendMessage("Invalid code template!", ChatType.FAIL);
                                    return 1;
                                }

                                String data = template.get("code").getAsString();

                                MutableText text = new LiteralText(
                                    "⇵ Click this message to view this code template in web!");
                                text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                        "https://dfonline.dev/edit/?template=" + data))
                                    .withColor(Formatting.AQUA));

                                CodeUtilities.MC.player.sendMessage(text, false);
                            }

                            return 1;
                        })
        );
    }
}