package moe.ku6.akamai.aimedb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;

@Slf4j
@AllArgsConstructor
public class AimeDbConnection extends SimpleChannelInboundHandler<AimeDbPacket> {
    @Getter
    private final SocketChannel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, AimeDbPacket packet) throws Exception {
        AimeDbServer.getInstance().getExecutorService().submit(() -> {
            try {
                var ret = HandlePacket(packet);
                if (ret == null) return;
                ctx.executor().submit(() -> {
                    ctx.write(ret);
                    ctx.flush();
                });
            } catch (Exception e) {
                log.error("Error handling packet", e);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

    }

    private ByteBuf HandlePacket(AimeDbPacket packet) {
        var handler = AimeDbServer.getInstance().GetHandler(packet.getType());
        if (handler != null) {
            var bodySize = handler.GetResponseBodySize(packet);
            if (bodySize % 16 != 0)
                throw new IllegalArgumentException("Invalid body size for handler %s, expected multiple of 16, got %d".formatted(handler, bodySize));

            var buf = Unpooled.copiedBuffer(new byte[bodySize]);
            buf.writerIndex(0);
            var msg = packet.getBody().copy();
            msg.readerIndex(0);
            handler.Handle(packet, msg, buf, channel);

            // create header+return buffer
            var responseType = handler.GetResponsePacketType();
            if (responseType == null) return null;

            var ret = Unpooled.copiedBuffer(new byte[32 + buf.capacity()]);
            ret.setShortLE(0x4, responseType.getId());
            ret.setShortLE(0x8, 1); // success

            // write body buffer into ret
            buf.readerIndex(0);
            ret.setBytes(32, buf);

            return ret;

        } else {
            ByteBuf buf = Unpooled.copiedBuffer(new byte[512]);
            switch (packet.getType()) {
                case CLIENT_END -> {
                    channel.close();
                    return null;
                }
                default -> {
                    log.warn("No handler found for packet type {}, packet {}", packet.getType(), packet.getHeader());
                    if (packet.getType().getResponseType() != null) {
                        // byte 4: response command id
                        buf.setShortLE(0x4, packet.getType().getResponseType().getId());
                        // byte 8: response
                        buf.setShortLE(0x8, 1); // success
                    }
                }
            }
            return buf;
        }
    }
}
