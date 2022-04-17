package io.github.codeutilities.mixin;

import io.github.codeutilities.event.EventRegister;
import io.github.codeutilities.event.impl.ShutdownEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MMinecraftClient {
    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo ci) {
        EventRegister.getInstance().dispatch(new ShutdownEvent());
    }
}
