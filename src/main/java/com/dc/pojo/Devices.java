package com.dc.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by xumepa on 9/24/17.
 */

public class Devices {

    @Autowired
    RestTemplate restTemplate;

    List<Device> devices = new ArrayList<>();
    private Device currentDevice;

    public void addDevice(UUID uuid, String address){
        Device device = new Device(uuid,address);
        devices.add(device);
    }
    public void addDevice(Device device){
        devices.add(device);
    }

    public List<Device> getDevices(){
        return devices;
    }

    //returns the device id
    public UUID discoverDevice(String address){

        String uri = "http://" + address + ":8080/echo/";
        System.out.println(uri);

        ResponseEntity<String> response
                = restTemplate.postForEntity(uri, null, String.class);

        System.out.println(response.getStatusCode());
        try {
            UUID uuid = UUID.fromString(response.getBody());
            return uuid;
        }catch(Exception ite){
            return null;
        }
//        ResponseEntity<UUID> body = response.getBody();
//        System.out.println("response : " + body.toString());
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device device) {
        this.currentDevice = device;
    }

    public void syncDevices(){
        for (Device device : devices) {
            String uri = "http://" + device.getIp() + ":8080/echo/";
            ResponseEntity<String> response
                    = restTemplate.postForEntity(uri, devices, String.class);
        }

    }
//    public void setLocalIP(){
//        InetAddress localhost = getLocalAddress();
//        ip = localhost.getAddress();
//        this.devices.put(currentDeviceUUID,ip.toString());
//    }


}
