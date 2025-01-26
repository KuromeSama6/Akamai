package moe.ku6.akamai.aimedb.packet;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import moe.ku6.akamai.util.Util;

@Getter
@ToString
public class AimeDbPacketHeader {
    public static final int MAGIC = 0xA13E;

    private final AimeDbPacketType type;
    private final int version;
    private final int length;
    private final int bodyLength;
    private final String gameId;
    private final int storeId;
    private final String keychip;

    public AimeDbPacketHeader(ByteBuf buf) {
        buf.resetReaderIndex();
        // skip 2 bytes magic
        buf.skipBytes(2);

        // 2 byte version string
        version = buf.readUnsignedShortLE();
        // 2 byte command id
        type = AimeDbPacketType.GetById(buf.readUnsignedShortLE());
        // 2 byte content length
        length = buf.readUnsignedShortLE();
        bodyLength = length - 32;

        // 2 byte result buffer (write) - skip?
        buf.skipBytes(2);

        // 5+1 byte game id (null terminated)
        gameId = Util.ReadToStringNullTerminated(buf, 6);

        // 4 byte store id
        storeId = buf.readIntLE();

        // 11+1 byte keychip
        keychip = Util.ReadToStringNullTerminated(buf, 12);

    }

}
