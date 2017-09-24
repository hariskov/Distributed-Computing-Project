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

    Map<UUID,String> devices = new HashMap<UUID,String>();
    private UUID currentDeviceUUID;

    public void addDevice(UUID uuid, String address){
        devices.put(uuid,address);
    }

    public Map<UUID,String> getDevices(){
        return devices;
    }

    public boolean hasDevice(UUID uuid){
        return devices.containsKey(uuid);
    }



    public void discoverDevice(String address){

        String uri = "http://" + address + ":8080/echo/";
        System.out.println(uri);

        ResponseEntity<String> response
                = restTemplate.postForEntity(uri, null, String.class);

        System.out.println(response.getStatusCode());
        try {
            UUID uuid = UUID.fromString(response.getBody());
            this.addDevice(uuid,address);
        }catch(Exception ite){
            //
        }
//        ResponseEntity<UUID> body = response.getBody();
//        System.out.println("response : " + body.toString());
    }

    public UUID getCurrentDeviceUUID() {
        return currentDeviceUUID;
    }

    public void setCurrentDeviceUUID(UUID currentDeviceUUID) {
        this.currentDeviceUUID = currentDeviceUUID;
    }
}
