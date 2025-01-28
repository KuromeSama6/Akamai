package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;
import moe.ku6.akamai.service.sega.allnet.KeychipService;

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
        // Update session place id
        var repo = KeychipService.getInstance().getSessionRepo();
        var session = repo.FindByKeychipAndGame(packet.getHeader().getKeychip(), packet.getHeader().getGameId());
        if (session == null) return;

        session.setPlaceId(packet.getHeader().getPlaceId());
        repo.save(session);
    }
}
