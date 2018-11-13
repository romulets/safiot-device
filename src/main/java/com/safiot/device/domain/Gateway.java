package com.safiot.device.domain;


import com.safiot.device.infra.Base64;
import com.safiot.device.infra.SecretKeyCipher;
import com.safiot.device.infra.Serializer;
import lombok.Setter;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

public class Gateway {

    private Map<UUID, Device> devices;

    @Setter
    private PublicKey serverKey;

    public Gateway(Map<UUID, Device> devices) {
        this.devices = devices;
    }

    public void sendMessage(byte[] encryptedMessage) throws GeneralSecurityException, IOException, ClassNotFoundException{

        System.out.println(String.format("GATEWAY RECEBEU       %s", Base64.encodeBase64(encryptedMessage)));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, serverKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        Message message = (Message) Serializer.fromString(new String(decryptedMessage));

        System.out.println(String.format("GATEWAY DECIFROU       %s", Base64.encodeBase64(message.getMessage())));

        Device device = devices.get(message.getMac());
        byte[] secondLevel = SecretKeyCipher.encrypt(message.getMessage(), device.getGatewayKey());

        System.out.println(String.format("GATEWAY CIFROU       %s", Base64.encodeBase64(secondLevel)));

        System.out.println();
        System.out.println("--");
        System.out.println();

        device.receiveMessage(secondLevel);
    }


}
