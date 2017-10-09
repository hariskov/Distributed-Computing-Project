package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Created by xumepa on 9/17/17.
 */

@Controller
@RequestMapping(value = "/echo")
public class EchoController {

    @Autowired
    DeviceManager deviceManager;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<String> exists(){
        return ResponseEntity.ok().body(deviceManager.getCurrentDevice().getUuid().toString());
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public void discover() throws IOException {
        deviceManager.discoverDevices();
    }

    @RequestMapping(value="/syncDevices", method = RequestMethod.POST)
    public void syncDevices(@RequestBody List<Device> newDevices){
        System.out.println(newDevices.size());
        newDevices.forEach(e->deviceManager.addDevice(e));
    }

}
