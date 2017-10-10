package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 9/24/17.
 */

@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    VotingManager manager;

    @RequestMapping(value="/startVote",method = RequestMethod.GET)
    public void startVote(){
        Vote vote = manager.createVote("Vote");
        if(vote!=null) {
            deviceManager.getDevices().forEach(k -> manager.getNetworkVotes(k, vote));
        }
    }

    @RequestMapping(value="/getVote",method = RequestMethod.POST)
    public ResponseEntity<Object> voting(@RequestBody Vote vote){
        List<Vote> localVotes = manager.getVotes().stream().filter(e->e.getVote() == vote.getVote()).collect(Collectors.toList());

        if(localVotes.size()>0){
            return ResponseEntity.ok("ad");
        }else {
            Random randomizer = new Random();
            Device random = deviceManager.getDevices().get(randomizer.nextInt(deviceManager.getDevices().size()));
            return ResponseEntity.ok(random);
        }
        //
//        if(randomNum>5){
//            return ResponseEntity.ok(true);
//        }else{
//            return ResponseEntity.ok(false);
//        }
    }

}
