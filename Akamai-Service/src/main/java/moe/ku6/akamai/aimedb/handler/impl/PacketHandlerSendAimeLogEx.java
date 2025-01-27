package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;

@Slf4j
public class PacketHandlerSendAimeLogEx extends AimeDbPacketHandler {
    @Override
    public AimeDbPacketType GetPacketType() {
        return AimeDbPacketType.SEND_AIME_LOG_EX;
    }

    @Override
    public int GetResponseBodySize(AimeDbPacket packet) {
        return 32;
    }

    @Override
    public void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf out, SocketChannel ctx) {
        // read data
        var aimeId = msg.getUnsignedIntLE(0);
        var status = msg.getUnsignedIntLE(4);
        var userId = msg.getLongLE(8);
        var placeId = msg.getUnsignedIntLE(44);
        log.info("AIME log, id={}, status={}, userId={}, placeId={}", aimeId, status, userId, placeId);

        for (int i = 0; i < 20; i++) {
            out.writeByte(1);
        }
    }
}
