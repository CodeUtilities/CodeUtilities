package io.github.codeutilities.mixin;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.util.hypercube.rank.HypercubeRank;
import io.github.codeutilities.util.hypercube.rank.HypercubeUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public class MChatComponent {
    private static double cooldown = System.currentTimeMillis();
    private static String lastMessage = "";

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.getType() == MessageType.CHAT || packet.getType() == MessageType.SYSTEM) {

            // This is to prevent ReceiveChatEvent duplicating for some unknown reason.
            if (Objects.equals(lastMessage, packet.getMessage().getString())) {
                if (!((System.currentTimeMillis() - cooldown) >= 2)) {
                    return;
                }
            }

            cooldown = System.currentTimeMillis();
            lastMessage = packet.getMessage().getString();

            ReceiveChatEvent event = new ReceiveChatEvent(packet.getMessage());
            EventManager.getInstance().dispatch(event);
            if (event.isCancelled()) {
                ci.cancel();
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

}
