package io.github.codeutilities.features.commands.afk;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.EventManager;
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
        EventManager.getInstance().register(KeyPressEvent.class, (event -> {
            if (Config.getBoolean("autoafk")) {
                afkTick = 0;

                if (afk) {
                    CodeUtilities.MC.player.sendChatMessage("/afk");
                }
            }
        }));

        EventManager.getInstance().register(TickEvent.class, (event -> {
            if (Config.getBoolean("autoafk")) {
                afkTick += 1;

                if (afkTick >= Config.getInteger("autoafk_time")) {
                    if (!afk) {
                        CodeUtilities.MC.player.sendChatMessage("/afk");
                    }
                }
            }
        }));

        EventManager.getInstance().register(ReceiveChatEvent.class, (event -> {
            String message = event.getMessage().getString();

            //Afk Reply
            try {
                if (afk) {
                    Pattern pattern = Pattern.compile("^\\[(.+) → You\\] (.+)$");
                    Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {

                        CodeUtilities.MC.player.sendChatMessage("/msg " + matcher.group(1) + " "
                                + Config.getString("autoafk_response").replace("%player", matcher.group(1)));

                        afkMessages.add(new HypercubePrivateMessage(matcher.group(1), matcher.group(2)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
