package io.github.codeutilities.features.afk;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.EventRegister;
import io.github.codeutilities.event.impl.ChatReceivedEvent;
import io.github.codeutilities.event.impl.TickEvent;
import io.github.codeutilities.event.impl.system.KeyPressEvent;
import io.github.codeutilities.event.listening.EventWatcher;
import io.github.codeutilities.event.listening.IEventListener;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.hypercube.HypercubePrivateMessage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfkFeature implements Loadable {
    public static boolean afk = false;
    public static int afkTick = 0;
    public static ArrayList<HypercubePrivateMessage> afkMessages = new ArrayList<>();


    @Override
    public void load() {
        EventRegister.getInstance().registerListener(new AfkFeature.EventListener());
    }

    public static class EventListener implements IEventListener {

        @EventWatcher
        public void onKeyPressed(KeyPressEvent event) {
            if (Config.getConfig().json().get("Auto AFK").getAsBoolean()) {
                afkTick = 0;

                if (afk) {
                    CodeUtilities.MC.player.sendChatMessage("/afk");
                }
            }
        }

        @EventWatcher
        public void onTick(TickEvent event) {
            if (Config.getConfig().json().get("Auto AFK").getAsBoolean()) {
                afkTick += 1;

                if (afkTick >= Config.getConfig().json().get("Auto AFK Time").getAsInt()) {
                    if (!afk) {
                        CodeUtilities.MC.player.sendChatMessage("/afk");
                    }
                }
            }
        }

        @EventWatcher
        public void onChatMessageReceived(ChatReceivedEvent event) {
            String message = event.getMessage().getString();

            //Afk Reply
            try {
                if (afk) {
                    Pattern pattern = Pattern.compile("^\\[(.+) → You\\] (.+)$");
                    Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {

                        CodeUtilities.MC.player.sendChatMessage("/msg " + matcher.group(1) + " "
                                + Config.getConfig().json().get("AFK Response").getAsString()
                                .replace("%player", matcher.group(1)));

                        afkMessages.add(new HypercubePrivateMessage(matcher.group(1), matcher.group(2)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
