package moe.ku6.akamai.aimedb.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class AimeDbPacket {
    private final AimeDbPacketType type;
    private final AimeDbPacketHeader header;
    private final ByteBuf body;

    public AimeDbPacket(ByteBuf buf) {
        // first 32 bytes
        header = new AimeDbPacketHeader(buf.copy().slice(0, 32));
        type = header.getType();
        body = buf.copy().slice(32, header.getBodyLength());
    }
}
