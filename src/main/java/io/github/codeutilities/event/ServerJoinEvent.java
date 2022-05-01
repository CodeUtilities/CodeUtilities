package io.github.codeutilities.event;

import io.github.codeutilities.event.system.Event;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

public class ServerJoinEvent implements Event {
    private final GameJoinS2CPacket packet;

    public ServerJoinEvent(GameJoinS2CPacket packet) {
        this.packet = packet;
    }

    public GameJoinS2CPacket getPacket() {
        return packet;
    }
}
