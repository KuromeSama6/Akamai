package moe.ku6.akamai.util.sega;

import lombok.experimental.UtilityClass;
import moe.ku6.akamai.util.JsonWrapper;

import java.security.SecureRandom;

@UtilityClass
public class ALLNetUtil {
    public static String GenerateKeychipId() {
        // A + 10 random digits
        return "A" + (1_000_000_000L + new SecureRandom().nextLong(9_000_000_000L));
    }
}
