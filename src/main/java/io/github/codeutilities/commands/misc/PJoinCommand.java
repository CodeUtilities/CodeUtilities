package io.github.codeutilities.commands.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.brigadier.CommandDispatcher;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.arguments.PlayerArgumentType;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.util.Regex;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class PJoinCommand implements Command {
	
	private boolean awaitingJoin = false;

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	
    	MinecraftClient mc = CodeUtilities.MC;
    	
    	EventManager.getInstance().register(ReceiveChatEvent.class, (event) -> {
    		
            if (this.awaitingJoin) {
            	
            	String stripped = event.getMessage().getString();
            	
                String msg = stripped.replaceAll("§.", "");
                if (msg.startsWith("                                       \n")) {
                    if (msg.contains(" is currently at spawn\n")) {
                        ChatUtil.sendMessage("This player is not in a plot.", ChatType.FAIL);
                    } else {
                        // PLOT ID
                        Regex pattern = Regex.of("\\[[0-9]+]\n");
                        Matcher matcher = pattern.getMatcher(msg);
                        String id = "";
                        while (matcher.find()) {
                            id = matcher.group();
                        }
                        id = id.replaceAll("\\[|]|\n", "");

                        String cmd = "/join " + id;

                        if (cmd.matches("/join [0-9]+")) {
                            mc.player.sendChatMessage(cmd);
                        } else {
                            ChatUtil.sendMessage("Error while trying to join the plot.", ChatType.FAIL);
                        }

                    }
                    
                    this.awaitingJoin = false;
                }
            }
            
    	});
    	
    	
    	
        cd.register(literal("pjoin")
                .then(argument("player", PlayerArgumentType.player())
                        .executes(ctx -> {
                            try {
                                return run(CodeUtilities.MC, ctx.getArgument("player", String.class));
                            } catch (Exception e) {
                                ChatUtil.sendMessage("Error while attempting to execute the command.", ChatType.FAIL);
                                e.printStackTrace();
                                return -1;
                            }
                        })
                )
        );
    }


    @Override
    public String getDescription() {
        return "[blue]/pjoin <player>[reset]\n"
                + "\n"
                + "Join the plot the specified player is currently playing.";
    }

    @Override
    public String getName() {
        return "/pjoin";
    }


    private int run(MinecraftClient mc, String player) {

        if (player.equals(mc.player.getName().asString())) {
            ChatUtil.sendMessage("You cannot use this command on yourself!", ChatType.FAIL);
            return -1;
        }

        mc.player.sendChatMessage("/locate " + player);

        this.awaitingJoin = true;
        ChatUtil.sendMessage("Joining the plot §e" + player + "§b is currently playing...", ChatType.INFO_BLUE);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (this.awaitingJoin) {
                ChatUtil.sendMessage("Timeout error while trying to join the plot.", ChatType.FAIL);
            }
            this.awaitingJoin = false;
        }).start();
        return 1;
    }
}
