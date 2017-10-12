package com.dc.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.util.*;

/**
 * Created by xumepa on 9/24/17.
 */

@Service
public class DeviceManager {

    @Autowired
    RestTemplate restTemplate;

    InetAddress localhost = getLocalAddress();
    List<Device> devices = new ArrayList<>();
    private Device currentDevice;
    private Device server;

    public DeviceManager(){
        currentDevice = new Device();
        currentDevice.setUuid(UUID.randomUUID());
        currentDevice.setIp(localhost.getHostAddress());
        addDevice(currentDevice);
    }

    public void addDevice(Device device){
        // should be unique
        if(devices.stream().filter(e->e.getIp() == device.getIp()).count()!=0){

        }


        if(devices.stream().filter(e->e.getUuid().equals(device.getUuid())).count()==0){
            devices.add(device);
        }
    }

    public List<Device> getDevices(){
        return devices;
    }

    //returns the device id
    public Device discoverDevice(String address){

        String uri = "http://" + address + ":8080/echo/";

        try {
            ResponseEntity<Device> response
                    = restTemplate.postForEntity(uri, null, Device.class);
            System.out.println(response.getBody());
            return response.getBody();
        }catch (Exception e){
            return null;
        }
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device device) {
        this.currentDevice = device;
    }

    public void syncDevices(List<Vote> votes){
        for (Device device : devices) {
            if(device == currentDevice){
                continue;
            }
            try {
                String uri = "http://" + device.getIp() + ":8080/echo/syncDevices";
                System.out.println(device.getUuid());
                restTemplate.postForEntity(uri, devices, Object.class);
                uri = "http://" + device.getIp() + ":8080/echo/syncVotes";
                restTemplate.postForEntity(uri, votes, Object.class);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public Device getServer() {
        return server;
    }

    public void setServer(Device server) {
        this.server = server;
    }

    public void discoverDevices() {
        if(1==1){
            Device device = discoverDevice("192.168.43.214");
            if(device!=null){
                addDevice(device);
                return;
            }
        }
        byte[] ip = localhost.getAddress();

        for (int i = 1; i <= 254; i++) {
            try {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);
                if(address.getHostAddress().equals(localhost.getHostAddress())){
                    continue;
                }
                if (address.isReachable(100) ) {
                    Device discoveredDevice = discoverDevice(address.toString().substring(1));
                    if (discoveredDevice != null) {
                        addDevice(discoveredDevice);
                    }
                }
                //                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        private InetAddress getLocalAddress(){
            try {
                Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
                while( b.hasMoreElements()) {
                    for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
                        if (f.getAddress().isSiteLocalAddress()) {
                            return f.getAddress();
                        }

//                    if(f.getAddress().isLoopbackAddress()){
//                        return f.getAddress();
//                    }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }


            try {
                return InetAddress.getByName("145.97.145.112");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return null;
        }
}
