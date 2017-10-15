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

import java.util.List;
import java.util.Random;
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

//    @RequestMapping(value="/startVote",method = RequestMethod.GET)
    @PostMapping("/startVote")
    public ResponseEntity startVote(@RequestBody String voteType){
        Vote vote = manager.createVote(voteType);
        if(vote!=null) {
            for (Device device : deviceManager.getDevices()) {
                manager.getNetworkVotes(device, vote);
//                manager.putVote(voteType, device, result);
            }
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/newVote")
    public ResponseEntity newVote(@RequestBody Vote vote){
        System.out.println("abcbde123");
        Vote localVotes = manager.getVotes().stream().filter(e->e.getVoteStr().equals(vote.getVoteStr())).findFirst().orElse(null);
        Object result;
        if(localVotes == null){
            result = null;
        }else {
            Random randomizer = new Random();
            Device random = deviceManager.getDevices().get(randomizer.nextInt(deviceManager.getDevices().size()));
            result = random;
        }

        localVotes.addVote(deviceManager.getCurrentDevice(),result);
        return ResponseEntity.ok(null);
    }

//    @RequestMapping(value="/getVoteStr",method = RequestMethod.POST)
    @PostMapping("/receiveVote")
    public ResponseEntity<Object> voting(@RequestBody Vote vote){
        Vote localVotes = manager.getVotes().stream().filter(e->e.getVoteStr() == vote.getVoteStr()).findFirst().orElse(null);

        System.out.println(vote.toString());
            System.out.println("logged in voting");
        return ResponseEntity.ok(null);
    }

}
