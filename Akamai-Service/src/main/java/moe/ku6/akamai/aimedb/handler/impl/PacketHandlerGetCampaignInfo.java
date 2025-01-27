package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;

@Slf4j
public class PacketHandlerGetCampaignInfo extends AimeDbPacketHandler {
    @Override
    public AimeDbPacketType GetPacketType() {
        return AimeDbPacketType.GET_CAMPAIGN_INFO;
    }

    @Override
    public int GetResponseBodySize(AimeDbPacket packet) {
        return packet.getHeader().getVersion() >= 0x3_03_0 ? 480 : 16;
    }

    @Override
    public void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf body, SocketChannel ctx) {
        // NOP
    }
}
