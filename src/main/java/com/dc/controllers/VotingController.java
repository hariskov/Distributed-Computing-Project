package com.dc.controllers;

import com.dc.pojo.Devices;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xumepa on 9/24/17.
 */


@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    @Autowired
    Devices devices;

    @Autowired
    VotingManager manager;

    @RequestMapping(value="/startVote",method = RequestMethod.GET)
    public void startVote(){
        manager.createVote("Vote");
        devices.getDevices().forEach((k,v)->manager.getNetworkVotes(v));
    }

    @RequestMapping(value="/getVote",method = RequestMethod.POST)
    public ResponseEntity<Boolean> voting(){

        int randomNum = ThreadLocalRandom.current().nextInt(0, 10);

        if(randomNum>5){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

}
