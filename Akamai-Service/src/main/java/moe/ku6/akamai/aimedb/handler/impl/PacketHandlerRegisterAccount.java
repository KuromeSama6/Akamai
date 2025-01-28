package moe.ku6.akamai.aimedb.handler.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCard;
import moe.ku6.akamai.service.sega.aimedb.AimeCardService;
import moe.ku6.akamai.util.RandomStringGenerator;
import moe.ku6.akamai.util.Util;
import org.joda.time.DateTime;

import java.security.SecureRandom;

@Slf4j
public class PacketHandlerRegisterAccount extends AimeDbPacketHandler {
    @Override
    public AimeDbPacketType GetPacketType() {
        return AimeDbPacketType.REGISTER_AIME_ACCOUNT;
    }

    @Override
    public int GetResponseBodySize(AimeDbPacket packet) {
        return 16;
    }

    @Override
    public void Handle(AimeDbPacket packet, ByteBuf msg, ByteBuf out, SocketChannel ctx) {
        var repo = AimeCardService.getInstance().getCardRepo();
        var accessCode = Util.ReadAIMEAccessCode(msg);
        var aimeId = new SecureRandom().nextInt(1, Integer.MAX_VALUE);

        log.info("RegisterAccount, accessCode={}, ip={}, aimeId={}", accessCode, ctx.remoteAddress().getAddress().toString(), aimeId);

        if (repo.FindByAimeId(aimeId) != null) {
            out.writeIntLE(0);
            out.writeByte(0);
            return;
        }

        var card = new AimeCard(aimeId, accessCode, ctx.remoteAddress().getAddress().toString());
        repo.save(card);

        out.writeIntLE(aimeId);
        out.writeByte(0);
    }
}
