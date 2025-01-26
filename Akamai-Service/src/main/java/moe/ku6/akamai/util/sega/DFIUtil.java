package moe.ku6.akamai.util.sega;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility class for encoding and decoding DFI content.
 */
@UtilityClass
public class DFIUtil {
    public static String Encode(String input) {
        try {
            // Compress using Deflater
            Deflater deflater = new Deflater();
            deflater.setInput(input.getBytes());
            deflater.finish();

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                while (!deflater.finished()) {
                    int count = deflater.deflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                deflater.end();

                // Base64 encode the compressed data
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error encoding DFI content", e);
        }
    }

    // Decode: Base64 decode, then decompress with Inflater
    public static String Decode(String input) {
        try {
            // Base64 decode
            byte[] compressedData = Base64.getDecoder().decode(input);

            // Decompress using Inflater
            Inflater inflater = new Inflater();
            inflater.setInput(compressedData);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                while (!inflater.finished()) {
                    int count = inflater.inflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                inflater.end();

                return outputStream.toString(); // Return the original string
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decoding DFI content", e);
        }
    }
}
