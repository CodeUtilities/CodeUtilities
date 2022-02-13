package io.github.codeutilities.commands.text;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.Codeutilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.WebUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.vehicle.Minecart;

public class UUIDCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        Minecraft mc = Codeutilities.MC;
        cd.register(literal("uuid")
                .executes(ctx -> {
                    showUUID(mc.player.getStringUUID());
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
            ChatUtil.error("Player not found!");
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
        ChatUtil.send(new TextComponent("UUID: " + uuid)
            .withStyle(Style.EMPTY
                .withHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new TextComponent("Click to copy")
                        .withStyle(ChatFormatting.GREEN)
                ))
                .withClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                        uuid
                    )
                )
                .withColor(ChatFormatting.GREEN)
            ));
    }
}
