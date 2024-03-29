package io.github.codeutilities.features.streamermode;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.util.chat.MessageGrabber;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StreamerModeHandler {

    public static boolean get(String key) {
        return Config.getBoolean(key);
    }

    public static String getString(String key) {
        return Config.getString(key);
    }

    public static boolean enabled() {
        return get("streamer");
    }

    public static boolean getOption(String key) {
        return enabled() && get(key);
    }

    public static boolean autoAdminV() {
        return getOption("streamerAutoAdminV");
    }

    public static boolean autoChatLocal() {
        return getOption("streamerAutoChatLocal");
    }

    public static boolean hideSpies() {
        return getOption("streamerSpies");
    }

    public static boolean hideSupport() {
        return getOption("streamerHideSupport");
    }

    public static boolean hideModeration() {
        return getOption("streamerHideModeration");
    }

    public static boolean hideAdmin() {
        return getOption("streamerHideAdmin");
    }

    public static boolean hideDMs() {
        return getOption("streamerHideDMs");
    }

    public static boolean hidePlotAds() {
        return getOption("streamerHidePlotAds");
    }

    public static boolean hideBuycraftUpdate() {
        return getOption("streamerHideBuycraftUpdate");
    }

    public static boolean hideRegexEnabled() {
        return getOption("streamerHideRegexEnabled");
    }

    public static String hideRegex() {
        return getString("streamerHideRegex");
    }

    // Looking for #handleMessage? This has been moved to mod.features.social.chat.message

    // Only triggers when joining DF or switching nodes
    public static void handleServerJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (!enabled()) return;

        // Run "/adminv off" and hide the message
        if (autoAdminV()) {
            CodeUtilities.MC.player.sendChatMessage("/adminv off");
        }

        // Run "/chat local" and hide the message
        if (autoChatLocal()) {
            CodeUtilities.MC.player.sendChatMessage("/c l");
        }

        // Hide messages
        if (autoAdminV() ^ autoChatLocal()) {
            MessageGrabber.hide(1);
        } else if (autoAdminV() && autoChatLocal()) {
            MessageGrabber.hide(2);
        }
    }
}