package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.features.commands.afk.AfkFeature;
import io.github.codeutilities.mixin.render.MChatHud;
import io.github.codeutilities.util.SoundUtil;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class AfkCommand implements Command {

    public static long cooldown = 0;

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
                literal("afk").executes(ctx -> {
                    long time = (System.currentTimeMillis() / 1000L) - cooldown;
                    if (!(time >= 2)) {
                        return 0;
                    }

                    cooldown = System.currentTimeMillis() / 1000L;
                    SoundUtil.playSound(Config.getSound("autoafk_sound"));

                    if (AfkFeature.afk) {
                        ChatUtil.sendMessage("You are no longer afk!", ChatType.SUCCESS);
                        ChatUtil.sendMessage("Here are the messages you were sent while afk.", ChatType.SUCCESS);
                        for (HypercubePrivateMessage message : AfkFeature.afkMessages) {
                            MutableText text = new LiteralText("(" + message.getDateFormat() + ") ")
                                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
                            text.append(message.getText());
                            CodeUtilities.MC.inGameHud.getChatHud().addMessage(text);
                        }

                        AfkFeature.afk = false;
                        AfkFeature.afkMessages.clear();
                        AfkFeature.players.clear();
                    } else {
                        ChatUtil.sendMessage("You are now afk!", ChatType.SUCCESS);
                        AfkFeature.afk = true;
                        AfkFeature.afkMessages.clear();
                        AfkFeature.players.clear();
                    }

                    return 0;
                })
        );
    }

    @Override
    public String getDescription() {
        return "[blue]/afk[reset]\n\n"
                + "Marks you as AFK. When you leave AFK, any\n"
                + "messages you received will be shown.\n";
    }

    @Override
    public String getName() {
        return "/afk";
    }
}

