package com.dc.controllers;

import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
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
    VotingManager votingManager;

//    @RequestMapping(value="/startVote",method = RequestMethod.GET)
    @PostMapping("/startVote")
    public ResponseEntity startVote(@RequestBody String voteType){
        Vote vote = votingManager.createVote(voteType);
        if(vote!=null) {
            votingManager.sendVotes(vote);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/newVote")
    public ResponseEntity newVote(@RequestBody Vote vote){
//        votingManager.generateVoteResult(vote);
        votingManager.setCurrentCirculatingVote(vote);
        //TODO fix this
//        localVotes.addVote(deviceManager.getCurrentDevice(),result);
        //TODO fix this

        return ResponseEntity.ok(null);
    }

//    @RequestMapping(value="/getVoteStr",method = RequestMethod.POST)
    @PostMapping("/receiveVote")
    public ResponseEntity<Object> voting(@RequestBody Vote vote){

        System.out.println(vote.toString());
//        vote.
//        this should put the Vote
        votingManager.applyVote(vote);

        return ResponseEntity.ok(null);
    }

}
