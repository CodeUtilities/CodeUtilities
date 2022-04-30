package io.github.codeutilities.features;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.config.internal.QueueMessages;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.RenderUtil;
import net.minecraft.client.toast.SystemToast;

public class SupportMessages implements Loadable {
    @Override
    public void load() {
        EventManager.getInstance().register(ReceiveChatEvent.class, (event -> {
            String message = event.getMessage().getString();

            if (message.startsWith("[SUPPORT] ")) {
                message = message.replace("[SUPPORT] ", "");

                if (message.endsWith(" joined the support queue.")) {
                    String player = message.replace(" joined the support queue.", "");

                    if (QueueMessages.TOAST == Config.getEnum("support_queuemessages", QueueMessages.class)) {
                        RenderUtil.sendToaster("Support Queue", player + " joined the queue.", SystemToast.Type.NARRATOR_TOGGLE);
                    }

                    event.setCancelled(true);
                }

                if (message.endsWith(" left the support queue.")) {
                    String player = message.replace(" left the support queue.", "");

                    if (QueueMessages.TOAST == Config.getEnum("support_queuemessages", QueueMessages.class)) {
                        RenderUtil.sendToaster("Support Queue", player + " left the queue.", SystemToast.Type.NARRATOR_TOGGLE);
                    }

                    event.setCancelled(true);
                }
            }
        }));
    }
}
