package com.dc.pojo;

import com.dc.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 9/24/17.
 */

@Component
@Configurable
public class DeviceManager {

    @Autowired
    @Qualifier(value="deviceService")
    private DeviceService deviceService;

    @Autowired
    MessageSendingOperations<String> messageSendingOperations;

    InetAddress localhost = getLocalAddress();
    List<Device> devices = new ArrayList<>();
    private Device currentDevice;
    private Device server;
    private boolean discoverable = false;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DeviceManager(){
        currentDevice = new Device();
        currentDevice.setIp(localhost.getHostAddress());
        currentDevice.setUuid(UUID.randomUUID());
        addDevice(currentDevice);
    }

    public void addDevice(Device device){
        // should be unique
//        devices.forEach(e->e.equals(device));
        Device localDevice = getDevice(device);
        if(localDevice==null){
            devices.add(device);
        }else{
            if((localDevice.getIp().equals(device.getIp())) && (localDevice.getUuid() != device.getUuid())){
                localDevice.setUuid(device.getUuid());
            }
        }
    }

    public Device getDevice(Device device){
        return devices.stream().filter(e->e.equals(device)).findFirst().orElse(null);
    }

    public List<Device> getDevices(){
        return devices;
    }

    //returns the device id
    public Device discoverDevice(String address){
        return deviceService.discoverDevice(address);
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device device) {
        this.currentDevice = device;
    }

    public void syncDevices(Device device){
            List<Device> receivedDevices = deviceService.syncDevices(device, devices);
            receivedDevices.stream().filter(e->!getDevices().contains(e)).collect(Collectors.toList()).forEach(this::addDevice); // do voting for this shit
    }

    public Device getServer() {
        return server;
    }

    public void setServer(Device server) {
        this.server = server;
    }

    public void discoverDevices() {
        byte[] ip = localhost.getAddress();
        logger.info("Discovery Started");

        // 2 for debugging
        for (int i = 2; i <= 254; i++) {
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
                    if(devices.size()==3){
                        break;
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

    public boolean containsAllDevices(List<Device> devices) {
        for (Device device : this.devices) {
            if(!contains(devices,device)){
                return false;
            }
        }
        return true;
    }

    private boolean contains(List<Device> devices, Device device) {
        for (Device device1 : devices) {
            if(device1.equals(device)){
                return true;
            }
        }
        return false;
    }

    @MessageMapping(value="/hello")
    public void callPlayersToNotDiscover() {
        String destination = "/broker/discoveryInProgress";
        this.messageSendingOperations.convertAndSend(destination,"true");
    }

    private void removeDevice(Device device) {
        try {
            Device deviceToRemove = devices.stream().filter(e -> e.equals(device)).findFirst().orElse(null);
            devices.remove(deviceToRemove);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void removeDevices(List<Device> devicesToRemove){
        for (Device device : devicesToRemove) {
            removeDevice(device);
        }
    }

    public boolean getDiscoverable() {
        return discoverable;
    }

    public void setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
    }
}
