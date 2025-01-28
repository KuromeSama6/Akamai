package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;
import moe.ku6.akamai.service.sega.aimedb.AimeCardService;
import moe.ku6.akamai.util.Util;

@Slf4j
public class PacketHandlerGetAccountEx extends AimeDbPacketHandler {
    @Override
    public AimeDbPacketType GetPacketType() {
        return AimeDbPacketType.GET_ACCOUNT_EX;
    }

    @Override
    public int GetResponseBodySize(AimeDbPacket packet) {
        return 272;
    }

    @Override
    public void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf out, SocketChannel ctx) {
        String accessCode = Util.ReadAIMEAccessCode(msg);

        var card = AimeCardService.getInstance().GetAndAccess(accessCode, ctx.remoteAddress().getAddress().toString());
        log.info("GetAccountEx, accessCode={}, ip={}, card={}", accessCode, ctx.remoteAddress().getAddress().toString(), card);

        // dword aime id
        if (card != null) {
            out.writeIntLE(card.getAimeId());
        } else {
            out.writeIntLE(0xffffffff);
        }

        // byte registration status + 3 padding
        out.writeByte(0);
        out.skipBytes(3);
    }
}
