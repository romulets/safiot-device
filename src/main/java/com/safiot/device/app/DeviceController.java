package com.safiot.device.app;

import com.safiot.device.domain.Device;
import com.safiot.device.domain.Gateway;
import com.safiot.device.domain.HttpsServer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DeviceController {

    private static Logger logger = Logger.getLogger(DeviceController.class.toString());

    Device deviceA;
    Device deviceB;
    Gateway gateway;
    HttpsServer httpsServer;

    public DeviceController() throws NoSuchAlgorithmException {
        deviceA = new Device();
        System.out.println();
        deviceB = new Device();
        System.out.println();

        List<Device> devices = new ArrayList<>();
        devices.add(deviceA);
        devices.add(deviceB);
        gateway = new Gateway(devices.stream().collect(Collectors.toMap(Device::getMac, i -> i)));
        httpsServer = new HttpsServer(devices.stream().collect(Collectors.toMap(Device::getMac, Device::getAppKey)), gateway);
    }

    public void listenMessages() throws IOException,GeneralSecurityException, ClassNotFoundException {
        long start = System.currentTimeMillis();

        System.out.println(String.format("Enviando mensagem via https server para %s", deviceB.getMac()));
        System.out.println();

        httpsServer.sendMessageTo(deviceB.getMac(), "Olá, teste");

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println();

        System.out.println(String.format("Duração %dms", end-start));
    }
}
