package io.github.codeutilities.event;

import io.github.codeutilities.event.system.Event;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;

import java.net.InetSocketAddress;

public class ServerLeaveEvent implements Event {
    private final DisconnectS2CPacket packet;

    public ServerLeaveEvent(DisconnectS2CPacket packet) { this.packet = packet; }

    public DisconnectS2CPacket getPacket() { return this.packet; }
}
