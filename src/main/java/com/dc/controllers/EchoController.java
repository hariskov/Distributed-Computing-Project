package com.dc.controllers;

import com.dc.pojo.*;
import com.dc.pojo.combos.VoteDevice;
import com.dc.services.NewVotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    NewVotingManager votingManager;

    @Autowired
    NewVotingService newVotingService;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<Device> exists(){
        return ResponseEntity.ok().body(deviceManager.getCurrentDevice());
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public ResponseEntity discover() throws IOException {
        deviceManager.discoverDevices();

        return ResponseEntity.ok(null);
    }

    @PostMapping("/getDevices")
    public ResponseEntity<Object> syncDevices(@RequestBody List<Device> devices){
        deviceManager.callPlayersToNotDiscover();
        devices.forEach(e->deviceManager.addDevice(e));
        return ResponseEntity.ok().body(deviceManager.getDevices());
    }

    @PostMapping("/syncVotes")
    public ResponseEntity<String> syncVotes(@RequestBody List<Vote> votes){
        votes.forEach(e->votingManager.setVotes(votes));
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/joinRequest")
    public ResponseEntity<Object> joinGameRequest(@RequestBody Device device){
        String voteStr = "join : " + device.getIp();
        newVotingService.sendStartVote(voteStr);
        SingleVote vote = votingManager.getDecidedVote(voteStr);
        return ResponseEntity.ok(vote.getAnswer());
    }

    @PostMapping("/removeDeviceAndLastVote")
    public ResponseEntity removeDeviceAndLastVote(@RequestBody VoteDevice voteDevice){

        deviceManager.removeDevices(voteDevice.getDevices());
        votingManager.removeVoteForDevices(voteDevice.getDevices(),voteDevice.getVoteStr());
        return ResponseEntity.ok(null);
    }

}
