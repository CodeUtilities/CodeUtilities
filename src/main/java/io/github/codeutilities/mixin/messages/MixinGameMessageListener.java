package io.github.codeutilities.mixin.messages;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.ModConfig;
import io.github.codeutilities.events.ChatReceivedEvent;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.DFInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinGameMessageListener {
    private MinecraftClient minecraftClient = MinecraftClient.getInstance();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        CodeUtilities.log(Level.DEBUG, "Is hypercube message: " + ChatUtil.verifyMessage(packet.getMessage()));
        if (DFInfo.isOnDF()) {
            if (packet.getLocation() == MessageType.CHAT || packet.getLocation() == MessageType.SYSTEM) {
                ChatReceivedEvent.onMessage(packet.getMessage(), ci);
                String text = packet.getMessage().getString();
                try {
                    this.updateVersion(packet.getMessage());
                    this.updateState(packet.getMessage());
                }catch (Exception e) {
                    CodeUtilities.log(Level.ERROR, "Error while trying to parse the chat text!");
                }
            }
        }
    }

    @Inject(method = "onTitle", at = @At("HEAD"), cancellable = true)
    private void onTitle(TitleS2CPacket packet, CallbackInfo ci) {
        TitleS2CPacket.Action action = packet.getAction();
        if (action == TitleS2CPacket.Action.ACTIONBAR) {
            if (packet.getText().getString().matches("DiamondFire  - .* CP - ??? .* Credits")) {
                DFInfo.currentState = DFInfo.State.LOBBY;
            }
        }
    }

    private void updateVersion(Text component) {
        String text = component.getString();
    
        if(!ChatUtil.verifyMessage(component)) {
            return;
        }
        
        if (text.matches("Current patch: .*\\. See the patch notes with \\/patch!")) {
            try {
                String patchText = text.replaceAll("Current patch: (.*)\\. See the patch notes with \\/patch!", "$1");

                DFInfo.isPatchNewer(patchText, "0"); //very lazy validation lol
                DFInfo.patchId = patchText;
                DFInfo.currentState = DFInfo.State.LOBBY;
                CodeUtilities.log(Level.INFO, "DiamondFire Patch " + DFInfo.patchId + " detected!");
            }catch (Exception e) {
                CodeUtilities.log(Level.INFO, "Error on parsing patch number!");
                e.printStackTrace();
            }
        }
    }

    private void updateState(Text component) {
        String text = component.getString();
        
        if(!ChatUtil.verifyMessage(component)) {
            return;
        }
        
        // Play Mode
        if (text.matches("Joined game: .* by .*") && text.startsWith("Joined game: ")) {
            DFInfo.currentState = DFInfo.State.PLAY;
        }

        // Build Mode
        if (minecraftClient.player.isCreative() && text.contains("?? You are now in build mode.") && text.startsWith("??")) {
            DFInfo.currentState = DFInfo.State.BUILD;
            if(ModConfig.getConfig().autotime) minecraftClient.player.sendChatMessage("/time " + ModConfig.getConfig().autotimeval);
        }

        // Dev Mode
        if (minecraftClient.player.isCreative() && text.contains("?? You are now in dev mode.") && text.startsWith("??")) {
            DFInfo.currentState = DFInfo.State.DEV;
            DFInfo.plotCorner = minecraftClient.player.getPos().add(10, -50, -10);
            
            if(ModConfig.getConfig().autoRC) minecraftClient.player.sendChatMessage("/rc");
            if(ModConfig.getConfig().autotime) minecraftClient.player.sendChatMessage("/time " + ModConfig.getConfig().autotimeval);
        }
    }
}
