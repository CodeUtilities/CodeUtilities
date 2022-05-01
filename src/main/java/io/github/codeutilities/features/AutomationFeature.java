package io.github.codeutilities.features;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.BuildModeEvent;
import io.github.codeutilities.event.DevModeEvent;
import io.github.codeutilities.event.PlayModeEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.chat.ChatUtil;
import io.github.codeutilities.util.chat.MessageGrabber;
import io.github.codeutilities.util.chat.text.TextUtil;
import net.minecraft.text.Text;

public class AutomationFeature implements Loadable {
    private static String tipPlayer = "";

    @Override
    public void load() {
        EventManager.getInstance().register(DevModeEvent.class, (event -> {
            if (Config.getBoolean("autotime")) {
                ChatUtil.executeCommandSilently("time " + Config.getLong("autotimeval"));
            }

            if (Config.getBoolean("autoRC")) {
                ChatUtil.executeCommandSilently("rc");
            }

            if (Config.getBoolean("autonightvis")) {
                ChatUtil.executeCommandSilently("nightvis");
            }

            if (Config.getBoolean("autolagslayer") && !LagslayerHUD.hasLagSlayer) {
                ChatUtil.executeCommandSilently("lagslayer");
            }

            if (Config.getBoolean("autoChatLocal")) {
                ChatUtil.executeCommandSilently("c l");
                MessageGrabber.hide(1);
            }
        }));

        EventManager.getInstance().register(BuildModeEvent.class, (event -> {
            if (Config.getBoolean("autotime")) {
                ChatUtil.executeCommandSilently("time " + Config.getLong("autotimeval"));
            }

            if (Config.getBoolean("autonightvis")) {
                ChatUtil.executeCommandSilently("nightvis");
            }

            if (Config.getBoolean("autoChatLocal")) {
                ChatUtil.executeCommandSilently("c l");
                MessageGrabber.hide(1);
            }
        }));

        EventManager.getInstance().register(PlayModeEvent.class, (event -> {
            if (Config.getBoolean("autolagslayer") && !LagslayerHUD.hasLagSlayer) {
                ChatUtil.executeCommandSilently("lagslayer");
            }

            if (Config.getBoolean("autoChatLocal")) {
                ChatUtil.executeCommandSilently("c l");
                MessageGrabber.hide(1);
            }
        }));

        EventManager.getInstance().register(ReceiveChatEvent.class, (event -> {
            Text text = event.getMessage();
            String stripped = text.getString();

            String msgWithColor = TextUtil.textComponentToColorCodes(text);

            if (Config.getBoolean("autoTip") && stripped.startsWith("⏵⏵ ")) {
                if (msgWithColor.matches("§x§a§a§5§5§f§f⏵⏵ §f§l\\w+§7 is using a §x§f§f§f§f§a§a§l2§x§f§f§f§f§a§a§lx§7 booster.")) {
                    tipPlayer = stripped.split("§f§l")[1].split("§7")[0];
                } else if (msgWithColor.matches("§x§a§a§5§5§f§f⏵⏵ §7Use §x§f§f§f§f§a§a\\/tip§7 to show your appreciation and receive a §x§f§f§d§4§2§a□ token notch§7!")) {
                    ChatUtil.executeCommand("/tip " + tipPlayer);
                }
            }
        }));
    }
}
