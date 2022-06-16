package io.github.codeutilities.event;

import io.github.codeutilities.event.system.Event;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

import java.net.InetSocketAddress;

public class ServerJoinEvent implements Event {
    private final GameJoinS2CPacket packet;
    private final InetSocketAddress address;

    public ServerJoinEvent(GameJoinS2CPacket packet, InetSocketAddress address) {
        this.packet = packet;
        this.address = address;
    }

    public GameJoinS2CPacket getPacket() {
        return packet;
    }
    public InetSocketAddress getAddress() { return address; }
}
