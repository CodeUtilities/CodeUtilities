package io.github.codeutilities.features;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.EventRegister;
import io.github.codeutilities.event.impl.ChatReceivedEvent;
import io.github.codeutilities.event.listening.EventWatcher;
import io.github.codeutilities.event.listening.IEventListener;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.chat.ChatUtil;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateMessageManipulator implements Loadable {
    @Override
    public void load() {
        EventRegister.getInstance().registerListener(new PrivateMessageManipulator.EventListener());
    }

    public static class EventListener implements IEventListener {

        @EventWatcher
        public void onChat(ChatReceivedEvent event) {
            String message = event.getMessage().getString();

            //Afk Reply
            try {
                Pattern pattern = Pattern.compile("^\\[(.+) → You\\] (.+)$");
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    event.setCancelled(true);
                    ChatUtil.sendMessage(
                            new HypercubePrivateMessage(matcher.group(1), matcher.group(2)).getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
