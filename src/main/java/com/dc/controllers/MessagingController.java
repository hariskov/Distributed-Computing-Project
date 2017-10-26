package com.dc.controllers;

import com.dc.pojo.SingleVote;
import com.dc.pojo.Vote;
import com.dc.pojo.VoteApply;
import com.dc.services.NewVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/voting")
public class MessagingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewVotingService newVotingService;

    @PostMapping(value="/startVote")
    public ResponseEntity startVote(@RequestBody String newVote){
        logger.info("got in : " + Thread.currentThread().getStackTrace()[0]);
        Vote vote = newVotingService.createVote(newVote);
        newVotingService.sendVote(vote);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveNewVote")
    public ResponseEntity sendIt(@RequestBody Vote vote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return ResponseEntity.ok(newVotingService.setTempVote(vote));
    }

    @PostMapping(value="/applyTempVote")
    public ResponseEntity applyTempVote(@RequestBody Vote tempVote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return ResponseEntity.ok(newVotingService.applyTempVote(tempVote));
    }

    @PostMapping(value="/receiveVoteAnswer")
    public ResponseEntity<SingleVote> receiveVote(@RequestBody String voteStr){
        return newVotingService.getVoteAnswer(voteStr);
    }

    @PutMapping(value="/applyVote")
    public ResponseEntity applyVote(@RequestBody VoteApply vote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        newVotingService.applyVote(vote);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value="/askForAnswer",method = RequestMethod.POST)
    public ResponseEntity<String> askAnswer(@RequestBody String voteStr){
        return ResponseEntity.ok(newVotingService.getTempVote(voteStr).getVoteStr());
    }
}
