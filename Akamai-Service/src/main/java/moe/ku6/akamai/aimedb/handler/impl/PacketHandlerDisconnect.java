package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;

public class PacketHandlerDisconnect extends AimeDbPacketHandler {
    @Override
    public AimeDbPacketType GetPacketType() {
        return AimeDbPacketType.CLIENT_END;
    }

    @Override
    public int GetResponseBodySize(AimeDbPacket packet) {
        return 0;
    }

    @Override
    public void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf buf, SocketChannel ctx) {
        ctx.close();
    }
}
