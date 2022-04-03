package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Date;

public class PingCommand implements Command {

    private long proxyPing = 0;
    private long serverPing = 0;
    private boolean pingActive = false;

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        EventManager.getInstance().register(ReceiveChatEvent.class, (event) -> {
            String message = event.getMessage().getString();

            if (serverPing != 0) {
                if (message.contains("Use /plot help for plot commands.")) {
                    ChatUtil.sendMessage("Server Ping: " + (new Date().getTime() - serverPing) + "ms", ChatType.SUCCESS);

                    serverPing = 0;
                    event.setCancelled(true);
                }
            }

            if (message.contains("You are currently connected to")) {
                if (proxyPing != 0) {
                    ChatUtil.sendMessage("Proxy Ping: " + (new Date().getTime() - proxyPing) + "ms", ChatType.SUCCESS);

                    proxyPing = 0;
                    event.setCancelled(true);
                }
            }

            if (message.startsWith("Available servers: ")) {
                if (pingActive) {
                    pingActive = false;
                    event.setCancelled(true);
                }
            }
        });

        cd.register(
                literal("ping").executes(ctx -> {
                    CodeUtilities.MC.player.sendChatMessage("/server");
                    CodeUtilities.MC.player.sendChatMessage("/plot");

                    serverPing = new Date().getTime();
                    proxyPing = new Date().getTime();
                    pingActive = true;

                    return 0;
                })
        );
    }
}

