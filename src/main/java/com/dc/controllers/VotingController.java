package com.dc.controllers;

import com.dc.pojo.*;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xumepa on 9/24/17.
 */

@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    @Autowired
    VotingService votingService;

    @PostMapping("/startVote")
    public ResponseEntity startVote(@RequestBody String voteType){
        votingService.startNewVote(voteType);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveNewTempVote")
    public ResponseEntity newVote(@RequestBody Vote vote){

        votingService.processVote(vote);

        //TODO fix this
        // do interceptor to requrest all other devices for their temp votes -> make sure they are the same !
        // possibility : another machine doesnt have it YET -> keep requesting till it receives -> this will fix reliability issue !

        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveVote")
    public ResponseEntity<Object> voting(@RequestBody Vote vote){

        System.out.println(vote.toString());

        //        this should put the Vote

//        HashMap<Device, Object> receivedVotes = votingManager.getLastVote().getVoteMap();
//        HashMap<Device, Object> currentVotes = vote.getVoteMap();


        // when receive vote -> calculate it against current vote
//        if(sameVotes) {
//            votingManager.applyVote(vote);
//        }
        return ResponseEntity.ok(null);
    }

}
