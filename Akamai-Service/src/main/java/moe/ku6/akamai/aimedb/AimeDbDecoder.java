package moe.ku6.akamai.aimedb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketHeader;
import moe.ku6.akamai.util.Util;

import java.util.List;

@Slf4j
public class AimeDbDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
//        log.info("client read, {}, readable {}, writable {}", buf, buf.readableBytes(), buf.writableBytes());
        if (buf.readableBytes() < 32) {
            log.warn("invalid packet size. Expected at least 32 bytes, but got {}", buf.readableBytes());
            ctx.close();
            return;
        }

        if (buf.readableBytes() % 16 != 0) {
            log.warn("malformed packet; invalid padding. Expected padding to be a multiple of 16, but got {} (total {} bytes)", buf.readableBytes() % 16, buf.readableBytes());
            ctx.close();
            return;
        }

        // decrypt
        var decrypted = AimeDbCodec.Decrypt(buf);

//        log.info("Packet data: {}", Util.FormatByteBuf(decrypted));

        var magic = decrypted.getUnsignedShortLE(0);
        if (magic != AimeDbPacketHeader.MAGIC) {
            log.warn("invalid magic. Expected %02X, but got %02X".formatted(AimeDbPacketHeader.MAGIC, magic));
            ctx.close();
            return;
        }

        var packet = new AimeDbPacket(decrypted);
//        log.info("pkt header: {}", packet.getHeader());

        list.add(packet);
        // move buf reader index to the end
        buf.readerIndex(buf.writerIndex());
    }
}
