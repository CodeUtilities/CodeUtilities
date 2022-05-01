package io.github.codeutilities.features;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.Regex;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;
import net.minecraft.text.Text;

import java.util.regex.Matcher;

public class PrivateMessageManipulator implements Loadable {
    @Override
    public void load() {
        // TODO: Fix messages going to main chat with side chat enabled

        /*
        EventManager.getInstance().register(ReceiveChatEvent.class, (event -> {
            String message = event.getMessage().getString();

            try {
                Regex pattern = Regex.of("^\\[(.+) → You\\] (.+)$");
                Matcher matcher = pattern.getMatcher(message);

                if (matcher.find()) {
                    event.setCancelled(true);

                    Text msg = new HypercubePrivateMessage(matcher.group(1), matcher.group(2)).getText();
                    CodeUtilities.MC.player.sendSystemMessage(msg, CodeUtilities.MC.player.getUuid());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

         */
    }
}
