package com.safiot.device.infra;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class SecretKeyCipher {

    private static final String ALGORITHM = "AES";

    public static byte[] encrypt(byte[] plainText, SecretKey key) throws GeneralSecurityException {
        Cipher gatewayCipher = Cipher.getInstance(ALGORITHM);
        gatewayCipher.init(Cipher.ENCRYPT_MODE, key);
        return gatewayCipher.doFinal(plainText);
    }


    public static byte[] decrypt(byte[] plainText, SecretKey key) throws GeneralSecurityException {
        Cipher appCipher = Cipher.getInstance(ALGORITHM);
        appCipher.init(Cipher.DECRYPT_MODE, key);
        return appCipher.doFinal(plainText);
    }

}
