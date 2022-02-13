package io.github.codeutilities.util;

import io.github.codeutilities.Codeutilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ChatUtil {

    public static void send(Component msg) {
        if (Codeutilities.MC.player == null) return;
        Codeutilities.MC.player.displayClientMessage(msg,false);
    }

    public static void error(String s) {
        send(new TextComponent(s)
            .withStyle(ChatFormatting.RED));
    }

    public static void success(String s) {
        send(new TextComponent(s)
            .withStyle(ChatFormatting.GREEN));
    }

    public static void info(String s) {
        send(new TextComponent(s)
            .withStyle(ChatFormatting.AQUA));
    }
}
