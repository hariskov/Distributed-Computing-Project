package com.dc.controllers;

import com.dc.pojo.*;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
/**
 * Created by xumepa on 9/24/17.
 */

@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    private final static Logger logger = LoggerFactory.getLogger(VotingController.class);

    @Autowired
    VotingService votingService;

    @PostMapping("/startVote")
    public ResponseEntity startVote(@RequestBody String voteType){
        votingService.startNewVote(voteType);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/receiveNewSingleVote")
    public ResponseEntity newSingleVote(@RequestBody SingleVote vote){

        votingService.processSingleVote(vote);

//        votingService.processTempVote(vote);

        //TODO fix this
        // do interceptor to requrest all other devices for their temp votes -> make sure they are the same !
        // possibility : another machine doesnt have it YET -> keep requesting till it receives -> this will fix reliability issue !

        return ResponseEntity.ok(null);
    }

    @PostMapping("/canISend")
    public ResponseEntity<Boolean> checkSend(@RequestBody SingleVote vote){
        boolean contains = votingService.containsVote(vote);
        if(contains){
            return ResponseEntity.ok(null);
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/receiveNewTempVote")
    public ResponseEntity newVote(@RequestBody Vote vote){

        votingService.processTempVote(vote);

//        votingService.processTempVote(vote);

        //TODO fix this
        // do interceptor to requrest all other devices for their temp votes -> make sure they are the same !
        // possibility : another machine doesnt have it YET -> keep requesting till it receives -> this will fix reliability issue !

        return ResponseEntity.ok(null);
    }

    @PutMapping("/receiveVote")
    public void voting(@RequestBody Vote vote){

        logger.info("entered Voting : " + vote.getVoteStr());
        votingService.processVote(vote);
    }

}
