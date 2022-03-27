package io.github.codeutilities.util.chat;

import io.github.codeutilities.CodeUtilities;
import java.awt.Color;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;

public class ChatUtil {

    public static void playSound(SoundEvent sound) {
        playSound(sound, 1F);
    }

    public static void playSound(SoundEvent sound, float pitch) {
        playSound(sound, 2F, pitch);
    }

    public static void playSound(SoundEvent sound, float pitch, float volume) {
        if (sound != null) {
            CodeUtilities.MC.player.playSound(sound, volume, pitch);
        }
    }

    public static void chat(String message) {
        CodeUtilities.MC.player.chat(message);
    }

    public static void executeCommand(String command) {
        chat("/" + command.replaceFirst("^/", ""));
    }

    public static void sendMessage(String text) {
        sendMessage(new TextComponent(text), null);
    }

    public static void sendActionBar(Component text) {
        CodeUtilities.MC.gui.setOverlayMessage(text, false);
    }

    public static void sendMessage(TextComponent text) {
        sendMessage(text, null);
    }

    public static void sendMessage(String text, ChatType prefixType) {
        sendMessage(new TextComponent(text), prefixType);
    }

    public static void sendMessage(TextComponent text, ChatType prefixType) {
        if (CodeUtilities.MC.player == null) return;
        CodeUtilities.MC.player.sendMessage(new TextComponent(prefixType.getString()).append(text), UUID.randomUUID());
    }

    public static MutableComponent setColor(MutableComponent component, Color color) {
        Style colorStyle = component.getStyle().withColor(TextColor.fromRgb(color.getRGB()));
        component.setStyle(colorStyle);
        return component;
    }

}
