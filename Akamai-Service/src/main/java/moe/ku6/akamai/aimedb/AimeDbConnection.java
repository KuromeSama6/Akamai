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
import moe.ku6.akamai.aimedb.packet.AimeDbPacketHeader;
import moe.ku6.akamai.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        var resultBuffer = Unpooled.copiedBuffer(new byte[512]);
        HandlePacket(packet, resultBuffer);

        ctx.write(resultBuffer);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void HandlePacket(AimeDbPacket packet, ByteBuf buf) {
        switch (packet.getType()) {
            case CLIENT_END -> {
                channel.close();
                return;
            }
            default -> {
                if (packet.getType().getResponseType() != null) {
                    // byte 4: response command id
                    buf.setShortLE(0x4, packet.getType().getResponseType().getId());
                    // byte 8: response
                    buf.setShortLE(0x8, 1); // success
                }
            }
        }
    }
}
