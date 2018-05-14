package com.safiot.device.app;

import com.safiot.device.domain.Device;

import java.security.NoSuchAlgorithmException;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceController {

    private static Logger logger = Logger.getLogger(DeviceController.class.toString());

    Device device;

    public DeviceController() throws NoSuchAlgorithmException {
        this.device = new Device();

        logger.log(Level.INFO, "App Key: {0}", encodeBase64(device.getAppKey().getEncoded()));
        logger.log(Level.INFO, "Gateway Key: {0}", encodeBase64(device.getGatewayKey().getEncoded()));
    }

    public void listenMessages() {
        logger.log(Level.SEVERE, "NOT IMPLEMENTED");
    }

    private String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
