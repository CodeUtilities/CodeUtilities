package io.github.codeutilities.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.LiteralText;

public class ToasterUtil {

    public static void sendToaster(String title, String description, SystemToast.Type type) {
        sendToaster(new LiteralText(title), new LiteralText(description), type);
    }

    public static void sendToaster(LiteralText title, LiteralText description, SystemToast.Type type) {
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(type, title, description));
    }

}