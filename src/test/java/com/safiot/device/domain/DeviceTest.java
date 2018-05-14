package com.safiot.device.domain;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeviceTest {

    @Test
    public void encryptMessageMustUseTwoLevels() throws GeneralSecurityException {
        Device device = new Device();
        String plainText = "Talk is cheap. Show me the code.";

        Cipher appCipher = Cipher.getInstance(Device.ALGORITHM);
        appCipher.init(Cipher.DECRYPT_MODE, device.getAppKey());

        Cipher gatewayCipher = Cipher.getInstance(Device.ALGORITHM);
        gatewayCipher.init(Cipher.DECRYPT_MODE, device.getGatewayKey());

        byte[] digest = device.encryptMessage(plainText.getBytes());

        byte[] decryptedMessage = appCipher.doFinal(
                gatewayCipher.doFinal(digest)
        );

        assertEquals(plainText, new String(decryptedMessage));
    }

    @Test(expected = BadPaddingException.class)
    public void encryptMessageMustWrapAppInGateway() throws GeneralSecurityException {
        Device device = new Device();
        String plainText = "Talk is cheap. Show me the code.";

        Cipher appCipher = Cipher.getInstance(Device.ALGORITHM);
        appCipher.init(Cipher.DECRYPT_MODE, device.getAppKey());

        Cipher gatewayCipher = Cipher.getInstance(Device.ALGORITHM);
        gatewayCipher.init(Cipher.DECRYPT_MODE, device.getGatewayKey());

        byte[] digest = device.encryptMessage(plainText.getBytes());

        gatewayCipher.doFinal(
                appCipher.doFinal(digest)
        );

        fail("The execution should have thrown a BadPaddingException");
    }

    @Test
    public void decryptMessageMustUseTwoLevels() throws GeneralSecurityException {
        Device device = new Device();
        String plainText = "Talk is cheap. Show me the code.";

        Cipher appCipher = Cipher.getInstance(Device.ALGORITHM);
        appCipher.init(Cipher.ENCRYPT_MODE, device.getAppKey());

        Cipher gatewayCipher = Cipher.getInstance(Device.ALGORITHM);
        gatewayCipher.init(Cipher.ENCRYPT_MODE, device.getGatewayKey());

        byte[] digest = gatewayCipher.doFinal(
                appCipher.doFinal(plainText.getBytes())
        );

        byte[] decryptedMessage = device.decryptMessage(digest);

        assertEquals(plainText, new String(decryptedMessage));
    }

    @Test(expected = BadPaddingException.class)
    public void decryptMessageMustDecryptGatewayBeforeApp() throws GeneralSecurityException {
        Device device = new Device();
        String plainText = "Talk is cheap. Show me the code.";

        Cipher appCipher = Cipher.getInstance(Device.ALGORITHM);
        appCipher.init(Cipher.ENCRYPT_MODE, device.getAppKey());

        Cipher gatewayCipher = Cipher.getInstance(Device.ALGORITHM);
        gatewayCipher.init(Cipher.ENCRYPT_MODE, device.getGatewayKey());

        byte[] digest = appCipher.doFinal(
                gatewayCipher.doFinal(plainText.getBytes())
        );

        device.decryptMessage(digest);

        fail("The execution should have thrown a BadPaddingException");
    }

}
