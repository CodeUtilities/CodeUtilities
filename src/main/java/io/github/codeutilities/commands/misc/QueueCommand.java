package io.github.codeutilities.commands.misc;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.features.commands.queue.QueueEntry;
import io.github.codeutilities.util.WebUtil;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;

import java.util.LinkedHashSet;

public class QueueCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("queue")
                .executes(ctx -> {

                    try {
                        WebUtil.getAsync("https://twitch.center/customapi/quote/list?token=18a3878c", res -> {
                            String[] splitQueue = res.split("\\n");
                            LinkedHashSet<QueueEntry> queue = new LinkedHashSet<>();

                            int i = 0;
                            for (String entry : splitQueue) {
                                QueueEntry queueEntry = new QueueEntry(entry, i);
                                if(!QueueEntry.HIDDEN_ENTRIES.contains(queueEntry.getPlotId()==null?"?":queueEntry.getPlotId().toString())){
                                    i++;
                                    queue.add(
                                            new QueueEntry(entry, i)
                                    );
                                }
                            }

                            CodeUtilities.MC.player.playSound(SoundEvents.UI_TOAST_IN, 2F, 1F);

                            // Temporary: Show in chat instead of menu
                            ChatUtil.sendMessage(
                                    new LiteralText("\n§r §r §r §r §r §r §r §r §r §r §r §r ").append(
                                            new LiteralText("⏪  ")
                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x1f9947))).append(
                                                            new LiteralText("CodeUtilities Twitch Plot Queue  ")
                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x33ffa7))).append(
                                                                            new LiteralText("⏩")
                                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x1f9947)))
                                                                    ))), null);

                            for (QueueEntry entry : queue) {
                                MutableText entrymsg = new LiteralText("#" + entry.getPosition())
                                        .styled(style -> style.withColor(TextColor.fromRgb(0x00bbff)
                                                        ).withClickEvent(
                                                                new ClickEvent(
                                                                        ClickEvent.Action.RUN_COMMAND,
                                                                        "/queue hideandjoin "+entry.getPlotId()
                                                                ))
                                                        .withHoverEvent(
                                                                new HoverEvent(
                                                                        HoverEvent.Action.SHOW_TEXT,
                                                                        new LiteralText("§7Click to join!")
                                                                )
                                                        )
                                        ).append(
                                                new LiteralText("§8 - ").append(
                                                        new LiteralText(entry.getPlotId()==null?"?":entry.getPlotId().toString())
                                                                .styled(style -> style.withColor(TextColor.fromRgb(0x66e6ff))).append(
                                                                        new LiteralText("§8 - ").append(
                                                                                new LiteralText(entry.getStrippedDescription())
                                                                                        .styled(style -> style.withColor(TextColor.fromRgb(0xbff9ff)))
                                                                        ))));
                                if(entry.isBeta()) {
                                    entrymsg.append(
                                            new LiteralText("\n§r §r §r §r §r §r §r §r §r §r §r §r ↑ ")
                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x7a2626))).append(
                                                            new LiteralText("This plot may be on ")
                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0xc96363))).append(
                                                                            new LiteralText("Node Beta")
                                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0xd95104))).append(
                                                                                            new LiteralText(" ↑")
                                                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x7a2626)))
                                                                                    ))));
                                }
                                ChatUtil.sendMessage(entrymsg, null);
                            }

                            ChatUtil.sendMessage("");
                        });
                    } catch (Exception e) {
                        ChatUtil.sendMessage("Error while requesting");
                        e.printStackTrace();
                        return 0;
                    }

                    return 1;
                }).then(argument("type", StringArgumentType.string())
                        .then(argument("id", StringArgumentType.string())
                                .executes(ctx -> {
                                    String id = ctx.getArgument("id", String.class);
                                    String type = ctx.getArgument("type", String.class);

                                    if(!id.equals("null")) {

                                        if(type.equals("hideandjoin")) {

                                            QueueEntry.HIDDEN_ENTRIES.add(id);

                                            CodeUtilities.MC.player.sendChatMessage("/join " + id);

                                            CodeUtilities.MC.player.sendMessage(new LiteralText("⏩ ")
                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x34961d))
                                                            .withClickEvent(
                                                                    new ClickEvent(
                                                                            ClickEvent.Action.RUN_COMMAND,
                                                                            "/queue show "+id
                                                                    ))
                                                            .withHoverEvent(
                                                                    new HoverEvent(
                                                                            HoverEvent.Action.SHOW_TEXT,
                                                                            new LiteralText("§7Click to unhide!")
                                                                    )
                                                            )).append(
                                                            new LiteralText("Plot " + id + " hidden from queue. Click here to unhide!")
                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0xb3ffa1))).append(
                                                                    new LiteralText(" ⏪")
                                                                            .styled(style -> style.withColor(TextColor.fromRgb(0x34961d)))
                                                            )), false);
                                        }

                                        if(type.equals("show")) {
                                            QueueEntry.HIDDEN_ENTRIES.remove(id);

                                            CodeUtilities.MC.player.sendMessage(new LiteralText("⏩ ")
                                                    .styled(style -> style.withColor(TextColor.fromRgb(0x37a61c))).append(
                                                            new LiteralText("Plot " + id + " will now be shown in queue.")
                                                                    .styled(style -> style.withColor(TextColor.fromRgb(0xb3ffa1))).append(
                                                                    new LiteralText(" ⏪")
                                                                            .styled(style -> style.withColor(TextColor.fromRgb(0x37a61c)))
                                                            )), false);
                                        }

                                    } else {
                                        CodeUtilities.MC.player.sendMessage(new LiteralText("⏩ ")
                                                .styled(style -> style.withColor(TextColor.fromRgb(0x961d1d))).append(
                                                        new LiteralText("Invalid plot ID!")
                                                                .styled(style -> style.withColor(TextColor.fromRgb(0xffa1a1))).append(
                                                                new LiteralText(" ⏪")
                                                                        .styled(style -> style.withColor(TextColor.fromRgb(0x961d1d)))
                                                        )), false);
                                    }

                                    return 1;
                                })
                        )));

    }

    @Override
    public String getDescription() {
        return "[blue]/queue[reset]\n"
                + "\n"
                + "Checks the Plot Queue of Jeremaster's DiamondFire stream.";
    }

    @Override
    public String getName() {
        return "/queue";
    }
}