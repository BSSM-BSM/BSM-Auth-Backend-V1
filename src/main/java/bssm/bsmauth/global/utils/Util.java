package bssm.bsmauth.global.utils;

import java.security.SecureRandom;
import java.util.HexFormat;

public class Util {

    static SecureRandom secureRandom = new SecureRandom();

    public static String getRandomStr(int length) {
        byte[] randomBytes = new byte[length / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }
}
