package com.safiot.device.domain;

import com.safiot.device.infra.Base64;
import com.safiot.device.infra.SecretKeyCipher;
import com.safiot.device.infra.Serializer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpsServer {

    private static Logger logger = Logger.getLogger(HttpsServer.class.toString());

    private Map<UUID, SecretKey> devices;

    private PrivateKey privateKey;

    private Gateway gateway;

    private PublicKey publicKey;
    public HttpsServer(Map<UUID, SecretKey> devices, Gateway gateway) throws NoSuchAlgorithmException {
        this.devices = devices;

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();

        this.gateway = gateway;
        this.gateway.setServerKey(publicKey);
    }

    public void sendMessageTo(UUID mac, String text) throws IOException, GeneralSecurityException, ClassNotFoundException {
        System.out.println(String.format("HTTPS SERVER VAI ENVIAR         %s", text));

        SecretKey secretKey = devices.get(mac);
        byte[] firstLevel = SecretKeyCipher.encrypt(text.getBytes(), secretKey);

        System.out.println(String.format("HTTPS SERVER CIFROU         %s", Base64.encodeBase64(firstLevel)));

        Message message = Message.builder().mac(mac).message(firstLevel).build();
        String serializedMessage = Serializer.toString(message);

        System.out.println(String.format("HTTPS SERVER SERIALIZOU        %s", serializedMessage));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] secondLevel = cipher.doFinal(serializedMessage.getBytes());

        System.out.println(String.format("HTTP SERVER CIFROU        %s", Base64.encodeBase64(secondLevel)));

        System.out.println();
        System.out.println("--");
        System.out.println();

        gateway.sendMessage(secondLevel);
    }

}
