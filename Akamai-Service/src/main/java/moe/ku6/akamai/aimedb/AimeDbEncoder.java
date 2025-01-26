package moe.ku6.akamai.aimedb;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketHeader;
import moe.ku6.akamai.util.Util;

@Slf4j
public class AimeDbEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf buf) throws Exception {
        msg.writerIndex(0);
        // magic
        msg.setShortLE(0, AimeDbPacketHeader.MAGIC);

        // version - constant
        msg.setShortLE(2, 0x3087);

        // 2 bytes length
        msg.setShortLE(6, msg.capacity());

        // encrypt
        msg.writerIndex(msg.capacity());
//        log.info("msg: {}", Util.FormatByteBuf(msg));
        var encrypted = AimeDbCodec.Encrypt(msg);
//        log.info("encoded: {}", Util.FormatByteBuf(encrypted));

        ctx.writeAndFlush(encrypted);
    }
}
