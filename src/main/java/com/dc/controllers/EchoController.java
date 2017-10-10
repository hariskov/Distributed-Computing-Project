package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Device> exists(){
        return ResponseEntity.ok().body(deviceManager.getCurrentDevice());
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public void discover() throws IOException {
        deviceManager.discoverDevices();
    }

//    @PostMapping(value="/syncDevices", produces = "application/json", consumes = "application/json")
    @PostMapping("/syncDevices")
    public ResponseEntity<String> syncDevices(@RequestBody List<Device> a){
        a.forEach(e->deviceManager.addDevice(e));
        return ResponseEntity.ok().body(null);

//        newDevices.forEach(e->deviceManager.addDevice(e));
    }

}
