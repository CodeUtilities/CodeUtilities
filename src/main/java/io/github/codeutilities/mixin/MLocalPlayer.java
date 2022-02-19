package io.github.codeutilities.mixin;

import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.event.SendChatEvent;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MLocalPlayer {

    @Inject(method = "chat", at = @At("HEAD"), cancellable = true)
    private void chat(String string, CallbackInfo ci) {
        SendChatEvent event = new SendChatEvent(string);
        EventManager.getInstance().dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        EventManager.getInstance().dispatch(new TickEvent());
    }

}
