package com.dc.controllers;

import com.dc.pojo.*;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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

    @PutMapping(value="/receiveStage1Vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity newVote(@RequestBody Vote vote){
        return ResponseEntity.ok(votingService.processTempVote(vote));
//        return ResponseEntity.ok(null);
    }


    @Async
    @PutMapping(value = "/receiveStage2Vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity voting(@RequestBody SingleVote vote){
        votingService.processVote(vote);
        return ResponseEntity.ok(null);
    }

}
