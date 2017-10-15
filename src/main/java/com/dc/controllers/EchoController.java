package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by xumepa on 9/17/17.
 */

@Controller
@RequestMapping(value = "/echo")
public class EchoController {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    VotingManager votingManager;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<Device> exists(){
        return ResponseEntity.ok().body(deviceManager.getCurrentDevice());
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public void discover() throws IOException {
        deviceManager.discoverDevices();
    }

//    @PostMapping(value="/syncDevices", produces = "application/json", consumes = "application/json")
    @PostMapping("/getDevices")
    public ResponseEntity<Object> syncDevices(){
        return ResponseEntity.ok().body(deviceManager.getDevices());
    }

    @PostMapping("/requestSyncDevices")
    public void requestSyncDevices(){
        deviceManager.getDevices().forEach(e->deviceManager.syncDevices(e));
    }

    @PostMapping("/syncVotes")
    public ResponseEntity<String> syncVotes(@RequestBody List<Vote> votes){
        votes.forEach(e->votingManager.setVotes(votes));
        return ResponseEntity.ok().body(null);
    }
}
