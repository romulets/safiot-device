package com.safiot.device.domain;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class Device {

    public static final String ALGORITHM = "DES";

    @Getter
    private SecretKey gatewayKey;

    @Getter
    private SecretKey appKey;

    public Device() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        this.appKey = keyGenerator.generateKey();
        this.gatewayKey = keyGenerator.generateKey();
    }

    public byte[] encryptMessage(byte[] plainText) throws GeneralSecurityException {
        return encryptUsingGatewayKey(
                encryptUsingAppKey(plainText)
        );
    }

    private byte[] encryptUsingAppKey(byte[] plainText) throws GeneralSecurityException {
        Cipher appCipher = Cipher.getInstance(ALGORITHM);
        appCipher.init(Cipher.ENCRYPT_MODE, appKey);
        return appCipher.doFinal(plainText);
    }

    private byte[] encryptUsingGatewayKey(byte[] plainText) throws GeneralSecurityException {
        Cipher gatewayCipher = Cipher.getInstance(ALGORITHM);
        gatewayCipher.init(Cipher.ENCRYPT_MODE, gatewayKey);
        return gatewayCipher.doFinal(plainText);
    }

    public byte[] decryptMessage(byte[] digest) throws GeneralSecurityException{
        return decryptUsingAppKey(
                decryptUsingGatewayKey(digest)
        );
    }

    private byte[] decryptUsingAppKey(byte[] plainText) throws GeneralSecurityException {
        Cipher appCipher = Cipher.getInstance(ALGORITHM);
        appCipher.init(Cipher.DECRYPT_MODE, appKey);
        return appCipher.doFinal(plainText);
    }

    private byte[] decryptUsingGatewayKey(byte[] plainText) throws GeneralSecurityException {
        Cipher gatewayCipher = Cipher.getInstance(ALGORITHM);
        gatewayCipher.init(Cipher.DECRYPT_MODE, gatewayKey);
        return gatewayCipher.doFinal(plainText);
    }
}
