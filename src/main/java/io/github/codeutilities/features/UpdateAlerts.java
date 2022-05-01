package io.github.codeutilities.features;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.config.internal.QueueMessages;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.ServerJoinEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.RenderUtil;
import io.github.codeutilities.util.VersionUtil;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class UpdateAlerts implements Loadable {
    @Override
    public void load() {
        EventManager.getInstance().register(ServerJoinEvent.class, (event -> {
            int latestVersion = VersionUtil.getLatestVersion();
            int currentVersion = VersionUtil.getCurrentVersionInt();
            int versionsBehind = latestVersion - currentVersion;

            if (versionsBehind > 10) {
                MutableText message = new LiteralText("")
                        .append(new LiteralText(String.format("You are currently on build #%s of CodeUtilities, which is %s versions behind the latest (%s). ",
                                currentVersion, versionsBehind, latestVersion))
                                .styled(style -> style.withColor(Formatting.YELLOW)))
                        .append(new LiteralText("Click here to download the latest version!")
                                .styled(style -> {
                                    style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://codeutilities.github.io"));
                                    style.withColor(Formatting.AQUA);
                                    return style;
                                }));

                CodeUtilities.MC.player.sendMessage(message, false);
            }
        }));
    }
}
