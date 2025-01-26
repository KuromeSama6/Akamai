package moe.ku6.akamai.util.sega;

import lombok.experimental.UtilityClass;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

@UtilityClass
public class ZLib {
    public static byte[] Decompress(byte[] data, boolean nowrap) {
        Inflater inflater = new Inflater(nowrap);
        var buf = new byte[1024];
        inflater.setInput(data);
        try {
            var out = new java.io.ByteArrayOutputStream();
            while (!inflater.finished()) {
                int count = inflater.inflate(buf);
                out.write(buf, 0, count);
            }
            inflater.end();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error decompressing data", e);
        }
    }

    public static byte[] Decompress(byte[] data) {
        return Decompress(data, false);
    }

    public static byte[] Compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        var buf = new byte[1024];
        try {
            var out = new java.io.ByteArrayOutputStream();
            while (!deflater.finished()) {
                int count = deflater.deflate(buf);
                out.write(buf, 0, count);
            }
            deflater.end();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error compressing data", e);
        }
    }
}
