package io.github.codeutilities.features.streamermode.message;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.event.RecieveSoundEvent;
import io.github.codeutilities.event.system.EventManager;
import io.github.codeutilities.features.streamermode.StreamerModeListeners;
import io.github.codeutilities.util.chat.MessageGrabber;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class Message {

    private final GameMessageS2CPacket packet;
    private final Text text;
    private final CallbackInfo callback;
    private final MessageType type;

    private MessageCheck check;
    private boolean cancelled;

    public Message(GameMessageS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.text = packet.getMessage();
        this.callback = ci;
        this.type = MessageCheck.run(this);
        MessageFinalizer.run(this);
    }

    public GameMessageS2CPacket getPacket() {
        return packet;
    }

    public Text getText() {
        return text;
    }

    public CallbackInfo getCallback() {
        return callback;
    }

    public MessageType getType() {
        return type;
    }

    public MessageCheck getCheck() {
        return check;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public String getStripped() {
        return text.getString();
    }

    public void setCheck(MessageCheck check) {
        this.check = check;
    }

    public boolean typeIs(MessageType toCompare) {
        return type == toCompare;
    }

    /**
     * Cancels this message. Also cancels the associated sound if there is any, plus cancels following messages if they are part.
     */
    public void cancel() {
        cancelled = true;

        callback.cancel();

        if (type.hasSound()) {
            StreamerModeListeners.canceledSounds += 1;
        }

        MessageGrabber.hide(type.getMessageAmount() - 1);
        System.out.println("[CANCELLED] " + text.toString());
    }
}
