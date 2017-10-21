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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xumepa on 9/24/17.
 */

@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @PostMapping("/startVote")
    public ResponseEntity startVote(@RequestBody String voteType){
        Vote vote = votingManager.createVote(voteType);
        if(vote!=null) {
            votingManager.sendVotes(vote);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveNewTempVote")
    public ResponseEntity newVote(@RequestBody Vote vote){
//        votingManager.generateVoteResult(vote);


        if(votingManager.getTempVote() == null){
            votingManager.setCurrentCirculatingVote(vote);
        }

        if(votingManager.getTempVote().getVote().containsKey(deviceManager.getCurrentDevice())) {
            votingManager.addValueToCurrentTempVote();
        }

        for (Map.Entry<Device, Object> deviceObjectEntry : vote.getVote().entrySet()) {
            if(!votingManager.getTempVote().getVote().containsKey(deviceObjectEntry.getKey())){
                votingManager.getTempVote().addVote(deviceObjectEntry.getKey(),deviceObjectEntry.getValue());
            }
        }

        //TODO fix this
        // do interceptor to requrest all other devices for their temp votes -> make sure they are the same !
        // possibility : another machine doesnt have it YET -> keep requesting till it receives -> this will fix reliability issue !

        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveVote")
    public ResponseEntity<Object> voting(@RequestBody Vote vote){

        System.out.println(vote.toString());

        //        this should put the Vote

        HashMap<Device, Object> receivedVotes = votingManager.getLastVote().getVote();
        HashMap<Device, Object> currentVotes = vote.getVote();


        // when receive vote -> calculate it against current vote
//        if(sameVotes) {
//            votingManager.applyVote(vote);
//        }
        return ResponseEntity.ok(null);
    }

}
