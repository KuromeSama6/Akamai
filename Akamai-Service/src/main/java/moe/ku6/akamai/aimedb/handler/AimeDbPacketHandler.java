package moe.ku6.akamai.aimedb.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;

public abstract class AimeDbPacketHandler {
    public abstract AimeDbPacketType GetPacketType();
    public abstract int GetResponseBodySize(AimeDbPacket packet);
    public abstract void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf out, SocketChannel ctx);

    public AimeDbPacketType GetResponsePacketType() {
        return GetPacketType().getResponseType();
    }
}
