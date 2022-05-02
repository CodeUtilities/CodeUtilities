package io.github.codeutilities.mixin.game;

import io.github.codeutilities.event.ShutdownEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.features.sidedchat.ChatShortcut;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MMinecraftClient {
    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo ci) {
        EventManager.getInstance().dispatch(new ShutdownEvent());
    }

    @Inject(method = "openChatScreen", at = @At("HEAD"))
    public void openChatScreen(String text, CallbackInfo ci) {
        // set such that no shortcut is active when pressing 't'
        ChatShortcut.setCurrentChatShortcut(null);
    }
}
