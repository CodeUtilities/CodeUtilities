package io.github.codeutilities.features;

import io.github.codeutilities.event.*;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;

public class PlayerState implements Loadable {

    private static boolean onDiamondFire;
    private static Mode currentMode = Mode.NONE;

    public static boolean onDF() {
        return onDiamondFire;
    }

    public static Mode getMode() {
        return currentMode;
    }

    public enum Mode {
        // not sure the best approach to check spawn state -
        // maybe an event if they run (/s, /spawn, /node, /leave)?
        NONE,
        SPAWN,
        PLAY,
        BUILD,
        DEV
    }

    public void load() {
        EventManager.getInstance().register(PlayModeEvent.class, (event) -> {
            currentMode = Mode.PLAY;
        });
        EventManager.getInstance().register(BuildModeEvent.class, (event) -> {
            currentMode = Mode.BUILD;
        });
        EventManager.getInstance().register(DevModeEvent.class, (event) -> {
            currentMode = Mode.DEV;
        });

        EventManager.getInstance().register(ServerJoinEvent.class, (event) -> {
            // extra period is necessary
            if (event.getAddress().getHostName().equals("play.mcdiamondfire.com.")) {
                onDiamondFire = true;
                currentMode = Mode.SPAWN;
            }
        });

        EventManager.getInstance().register(ServerLeaveEvent.class, (event) -> {
            onDiamondFire = false;
            currentMode = Mode.NONE;
        });

        EventManager.getInstance().register(SendChatEvent.class, (event) -> {

            // we could consider moving command check to an event if its necessary
            if (PlayerState.onDF() && event.getMessage().startsWith("/")) {

                // remove first / and then trim it
                // this is necessary because "/       spawn" is a valid way of running commands
                String command = event.getMessage().substring(1).trim();



                if (command.equals("s") || command.equals("spawn")
                        || command.equals("leave") || command.startsWith("node ")) {

                    currentMode = Mode.SPAWN;
                }
            }

        });
    }



}
