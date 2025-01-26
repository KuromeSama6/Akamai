package moe.ku6.akamai.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class Util {
    public static String SHA256(String input) {
        if (input == null) return null;

        try {
            // Get an instance of the SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String ToUrlEncodedParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!sb.isEmpty()) sb.append('&');
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.toString();
    }

    public static String ReadToStringNullTerminated(ByteBuf buf, int length) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);

        var ret = new String(bytes).split("\0");
        return ret.length > 0 ? ret[0] : new String(bytes);
    }

    public static String FormatByteBuf(ByteBuf buf) {
        var bytes = ByteBufUtil.getBytes(buf);
        return IntStream.range(0, bytes.length)
                .mapToObj(i -> String.format("%02X", bytes[i] & 0xFF)) // Format each byte to hex
                .collect(Collectors.joining(" ")); // Join with spaces
    }
}
