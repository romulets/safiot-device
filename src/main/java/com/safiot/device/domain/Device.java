package com.safiot.device.domain;

import com.safiot.device.infra.Base64;
import com.safiot.device.infra.SecretKeyCipher;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.safiot.device.infra.Base64.encodeBase64;

public class Device {


    private static final String ALGORITHM = "AES";

    @Getter
    private UUID mac;

    @Getter
    private SecretKey gatewayKey;

    @Getter
    private SecretKey appKey;

    public Device() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        this.mac = UUID.randomUUID();
        this.appKey = keyGenerator.generateKey();
        this.gatewayKey = keyGenerator.generateKey();

        System.out.println(String.format("MAC Address: %s", mac.toString()));
        System.out.println(String.format("App Key: %s", encodeBase64(appKey.getEncoded())));
        System.out.println(String.format("Gateway Key: %s", encodeBase64(gatewayKey.getEncoded())));
    }


    public void receiveMessage(byte[] digest) throws GeneralSecurityException{
        System.out.println(String.format("DEVICE RECEBEU        %s",  Base64.encodeBase64(digest)));

        byte[] messageBytes = decryptUsingAppKey(
                decryptUsingGatewayKey(digest)
        );

        String message = new String(messageBytes);

        System.out.println(String.format("DEVICE RECEBEU FINAL       '%s'", message));
    }

    private byte[] decryptUsingAppKey(byte[] digest) throws GeneralSecurityException {
        System.out.println(String.format("DEVICE DECIFROU        %s",  Base64.encodeBase64(digest)));
        return SecretKeyCipher.decrypt(digest, appKey);
    }

    private byte[] decryptUsingGatewayKey(byte[] digest) throws GeneralSecurityException {
        System.out.println(String.format("DEVICE DECIFROU        %s",  Base64.encodeBase64(digest)));
        return SecretKeyCipher.decrypt(digest, gatewayKey);
    }
}
