package io.github.codeutilities.mixin.render;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.features.LagslayerHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MInGameHUD {
    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    private void renderStatusEffectOverlay(MatrixStack stack, CallbackInfo ci) {
        LagslayerHUD.onRender(stack);

        MinecraftClient mc = CodeUtilities.MC;
        TextRenderer tr = mc.textRenderer;
    }

    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
    private void renderScoreboardSidebar(CallbackInfo info) {
        MinecraftClient client = CodeUtilities.MC;
        if (client.options.debugEnabled) {
            info.cancel();
        }
    }
}