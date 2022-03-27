package io.github.codeutilities.commands.text;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.WebUtil;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class UUIDCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        MinecraftClient mc = CodeUtilities.MC;
        cd.register(literal("uuid")
                .executes(ctx -> {
                    showUUID(mc.player.getUuid().toString());
                    return 1;
                })
                .then(argument("player", StringArgumentType.string())
                        .executes(ctx -> {
                            String player = StringArgumentType.getString(ctx, "player");
                            WebUtil.getAsync("https://api.mojang.com/users/profiles/minecraft/" + player, (json) -> {
                                if (json.isBlank()) {
                                    showUUID(null);
                                } else {
                                    JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                                    showUUID(obj.get("id").getAsString());
                                }
                            });
                            return 1;
                        })
                )
        );
    }

    private void showUUID(String uuid) {
        if (uuid == null) {
            ChatUtil.sendMessage("Player not found!", ChatType.FAIL);
            return;
        }
        if (!uuid.contains("-")) {
            //add dashes
            StringBuilder sb = new StringBuilder(uuid);
            sb.insert(8, "-");
            sb.insert(13, "-");
            sb.insert(18, "-");
            sb.insert(23, "-");
            uuid = sb.toString();
        }

        CodeUtilities.MC.player.sendMessage(new LiteralText("UUID: " + uuid)
                .setStyle(Style.EMPTY
                .withHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new LiteralText("Click to copy")
                        .formatted(Formatting.GREEN)
                ))
                .withClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                        uuid
                    )
                )
                .withColor(Formatting.GREEN)
            ), false);
    }
}
