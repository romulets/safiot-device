package com.safiot.device.app;

public class Application {

    public static void main(String[] args) throws Exception{
        DeviceController deviceController = new DeviceController();

        deviceController.listenMessages();
    }

}
