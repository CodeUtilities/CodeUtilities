package io.github.codeutilities.features;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.Regex;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;

import java.util.regex.Matcher;

public class PrivateMessageManipulator implements Loadable {
    @Override
    public void load() {
        EventManager.getInstance().register(ReceiveChatEvent.class, (event -> {
            String message = event.getMessage().getString();

            try {
                Regex pattern = Regex.of("^\\[(.+) → You\\] (.+)$");
                Matcher matcher = pattern.getMatcher(message);

                if (matcher.find()) {
                    event.setCancelled(true);

                    Text msg = new HypercubePrivateMessage(matcher.group(1), matcher.group(2)).getText();
                    CodeUtilities.MC.inGameHud.getChatHud().addMessage(msg);
                }

                pattern = Regex.of("^\\[You → (.+)\\] (.+)$");
                matcher = pattern.getMatcher(message);

                if (matcher.find()) {
                    event.setCancelled(true);

                    Text msg = new HypercubePrivateMessage(matcher.group(1), matcher.group(2)).getText();
                    CodeUtilities.MC.inGameHud.getChatHud().addMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
