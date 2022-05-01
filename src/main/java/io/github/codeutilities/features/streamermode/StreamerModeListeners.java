package io.github.codeutilities.features.streamermode;

import io.github.codeutilities.event.PlayModeEvent;
import io.github.codeutilities.event.RecieveSoundEvent;
import io.github.codeutilities.event.ServerJoinEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.chat.ChatUtil;

public class StreamerModeListeners implements Loadable {
    public static int canceledSounds = 0;

    @Override
    public void load() {
        EventManager.getInstance().register(ServerJoinEvent.class, (event -> {
            if (StreamerModeHandler.autoAdminV()) {
                ChatUtil.executeCommandSilently("/adminv off");
            }
        }));

        EventManager.getInstance().register(PlayModeEvent.class, (event -> {
            if (StreamerModeHandler.autoAdminV()) {
                ChatUtil.executeCommandSilently("/adminv off");
            }
        }));

        EventManager.getInstance().register(RecieveSoundEvent.class, (event -> {
            if (canceledSounds >= 1) {
                canceledSounds -= 1;
                event.setCancelled(true);
            }
        }));
    }
}
