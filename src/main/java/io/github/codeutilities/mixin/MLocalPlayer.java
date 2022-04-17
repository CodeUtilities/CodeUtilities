package io.github.codeutilities.mixin;

import io.github.codeutilities.event.EventRegister;
import io.github.codeutilities.event.impl.ChatSentEvent;
import io.github.codeutilities.event.impl.TickEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MLocalPlayer {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void chat(String string, CallbackInfo ci) {
        ChatSentEvent event = new ChatSentEvent(string);
        EventRegister.getInstance().dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        EventRegister.getInstance().dispatch(new TickEvent());
    }

}
