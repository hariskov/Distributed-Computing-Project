package com.dc.controllers;

import com.dc.pojo.SingleVote;
import com.dc.pojo.Vote;
import com.dc.services.NewVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.Response;
import java.util.Arrays;

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
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return newVotingService.getVoteAnswer(voteStr);
    }

    @PutMapping(value="/applyVote")
    public void applyVote(@RequestBody SingleVote vote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        newVotingService.applyVote(vote);
    }

//    @PostMapping("/receiveInfo")
//    public ResponseEntity receiveInfo(){
//
//    }
}