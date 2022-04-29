package io.github.codeutilities.features;

import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.loader.Loadable;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindManager implements Loadable {

    private static final String CATEGORY = "key.category.codeutilities";
    private static final String PREFIX = "key.codeutilities.";

    private KeyBinding spawn, play, build, dev, fs100, fs500, node1, node2, node3, node4, node5, node6, node7, nodeBeta, chatGlobal, chatLocal, chatNone, resetInv, compactInv;

    @Override
    public void load() {
        spawn = keybind("spawn");
        play = keybind("play");
        build = keybind("build");
        dev = keybind("dev");
        fs100 = keybind("fs100");
        fs500 = keybind("fs500");
        node1 = keybind("node1");
        node2 = keybind("node2");
        node3 = keybind("node3");
        node4 = keybind("node4");
        node5 = keybind("node5");
        node6 = keybind("node6");
        node7 = keybind("node7");
        nodeBeta = keybind("nodeBeta");
        chatGlobal = keybind("chatGlobal");
        chatLocal = keybind("chatLocal");
        chatNone = keybind("chatNone");
        resetInv = keybind("resetInv");
        compactInv = keybind("compactInv");

        EventManager.getInstance().register(TickEvent.class, this::onTick);
    }

    private void onTick(TickEvent t) {
        if (spawn.wasPressed()) {
            ChatUtil.executeCommand("spawn");
        }
        if (play.wasPressed()) {
            ChatUtil.executeCommand("play");
        }
        if (build.wasPressed()) {
            ChatUtil.executeCommand("build");
        }
        if (dev.wasPressed()) {
            ChatUtil.executeCommand("dev");
        }
        if (fs100.wasPressed()) {
            ChatUtil.executeCommand("fs 100");
        }
        if (fs500.wasPressed()) {
            ChatUtil.executeCommand("fs 500");
        }
        if (node1.wasPressed()) {
            ChatUtil.executeCommand("server node1");
        }
        if (node2.wasPressed()) {
            ChatUtil.executeCommand("server node2");
        }
        if (node3.wasPressed()) {
            ChatUtil.executeCommand("server node3");
        }
        if (node4.wasPressed()) {
            ChatUtil.executeCommand("server node4");
        }
        if (node5.wasPressed()) {
            ChatUtil.executeCommand("server node5");
        }
        if (node6.wasPressed()) {
            ChatUtil.executeCommand("server node6");
        }
        if (node7.wasPressed()) {
            ChatUtil.executeCommand("server node7");
        }
        if (nodeBeta.wasPressed()) {
            ChatUtil.executeCommand("server beta");
        }
        if (chatGlobal.wasPressed()) {
            ChatUtil.executeCommand("chat global");
        }
        if (chatLocal.wasPressed()) {
            ChatUtil.executeCommand("chat local");
        }
        if (chatNone.wasPressed()) {
            ChatUtil.executeCommand("chat none");
        }
        if (resetInv.wasPressed()) {
            ChatUtil.executeCommand("rs");
        }
        if (compactInv.wasPressed()) {
            ChatUtil.executeCommand("rc");
        }
    }

    private KeyBinding keybind(String code) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
            PREFIX + code, InputUtil.Type.KEYSYM, -1, CATEGORY
        ));
    }
}
