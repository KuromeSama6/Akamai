package moe.ku6.akamai.util.sega;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
@UtilityClass
public class ALLNetDecoder {
    public static byte[] DecodeDFI(byte[] src, boolean base64, boolean nowrap) {
        var bytes = base64 ? Base64.getDecoder().decode(src) : src;
        return ZLib.Decompress(bytes, nowrap);
    }
}
