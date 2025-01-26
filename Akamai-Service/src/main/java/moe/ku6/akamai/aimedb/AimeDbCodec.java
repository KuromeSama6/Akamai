package moe.ku6.akamai.aimedb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class AimeDbCodec {
    private static final SecretKeySpec key = new SecretKeySpec("Copyright(C)SEGA".getBytes(StandardCharsets.UTF_8), "AES");

    public static ByteBuf Decrypt(ByteBuf buf) {
        try {
            var cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return Unpooled.copiedBuffer(cipher.doFinal(ByteBufUtil.getBytes(buf)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ByteBuf Encrypt(ByteBuf buf) {
        try {
            var cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            buf.readerIndex(0);
            buf.writerIndex(buf.capacity());
            return Unpooled.copiedBuffer(cipher.doFinal(ByteBufUtil.getBytes(buf)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
