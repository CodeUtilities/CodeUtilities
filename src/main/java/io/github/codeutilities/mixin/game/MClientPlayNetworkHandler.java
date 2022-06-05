package io.github.codeutilities.mixin.game;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.*;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.features.LagslayerHUD;
import io.github.codeutilities.features.PlayerState;
import io.github.codeutilities.features.commands.templates.TemplateStorageHandler;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageFinalizer;
import io.github.codeutilities.util.hypercube.rank.HypercubeRank;
import io.github.codeutilities.util.hypercube.rank.HypercubeUtil;
import io.github.codeutilities.util.template.TemplateUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import okhttp3.Call;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public class MClientPlayNetworkHandler {

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!RenderSystem.isOnRenderThread()) {
            return;
        }

        if (packet.getType() == MessageType.CHAT || packet.getType() == MessageType.SYSTEM) {
            MessageFinalizer.run(new Message(packet, ci));

            ReceiveChatEvent event = new ReceiveChatEvent(packet.getMessage());
            EventManager.getInstance().dispatch(event);
            if (event.isCancelled()) {
                ci.cancel();
            }

            String packetString = packet.getMessage().getString();

            if (packetString.equals("» You are now in dev mode.")) {
                DevModeEvent modeEvent = new DevModeEvent();
                EventManager.getInstance().dispatch(modeEvent);
            }

            if (packetString.equals("» You are now in build mode.")) {
                BuildModeEvent modeEvent = new BuildModeEvent();
                EventManager.getInstance().dispatch(modeEvent);
            }

            if (packetString.startsWith("» Joined plot") || packetString.startsWith("» Joined game")) {
                PlayModeEvent modeEvent = new PlayModeEvent();
                EventManager.getInstance().dispatch(modeEvent);
            }
        }
    }

    @Inject(method = "onTeam", at = @At("RETURN"))
    private void handleSetPlayerTeamPacket(TeamS2CPacket packet, CallbackInfo ci) {
        if (CodeUtilities.MC.player != null) {
            if (CodeUtilities.MC.getCurrentServerEntry() != null) {
                if (CodeUtilities.MC.getCurrentServerEntry().address.contains("mcdiamondfire.com")) {
                    if (packet.getPlayerNames().contains(CodeUtilities.MC.player.getName().getString())) {
                        for (HypercubeRank r : HypercubeRank.values()) {
                            if (r.getTeamName() == null)
                                continue;

                            if (packet.getTeamName().endsWith(r.getTeamName())) {
                                HypercubeUtil.setRank(r);
                                return;
                            }
                        }

                        HypercubeUtil.setRank(HypercubeRank.DEFAULT);
                    }
                }
            }
        }
    }

    @Inject(method = "onOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void onOverlayMessage(OverlayMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.getMessage().getString().matches("^CPU Usage: \\[▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮\\] \\(.*%\\)$")) {
            if (Config.getBoolean("cpuOnScreen")) {
                LagslayerHUD.updateCPU(packet);
                ci.cancel();
            }
        }
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"), cancellable = true)
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ClientConnection conn = CodeUtilities.MC.getNetworkHandler().getConnection();
        ServerJoinEvent event = new ServerJoinEvent(packet, ((InetSocketAddress)conn.getAddress()));
        EventManager.getInstance().dispatch(event);
    }

    @Inject(method = "onDisconnect", at = @At("RETURN"), cancellable = true)
    private void onDisconnect(DisconnectS2CPacket packet, CallbackInfo ci) {
        ServerLeaveEvent event = new ServerLeaveEvent(packet);
        EventManager.getInstance().dispatch(event);
    }

    @Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        RecieveSoundEvent event = new RecieveSoundEvent(packet);
        EventManager.getInstance().dispatch(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("RETURN"), cancellable = true)
    private void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        if (CodeUtilities.MC.player.isCreative()) {
            ItemStack stack = packet.getItemStack();
            if (TemplateUtil.isTemplate(stack)) {
                TemplateStorageHandler.addTemplate(stack);
            }
        }
    }
}
