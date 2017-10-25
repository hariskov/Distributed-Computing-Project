package com.dc.controllers;

import com.dc.pojo.NewVote;
import com.dc.pojo.SingleVote;
import com.dc.pojo.Vote;
import com.dc.services.NewVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
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
    public ResponseEntity startVote(@RequestBody Object newVote){
        logger.info("got in : " + Thread.currentThread().getStackTrace()[0]);
        NewVote vote = newVotingService.createVote(newVote);
        newVotingService.sendVote(vote);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/receiveNewVote")
    public ResponseEntity sendIt(@RequestBody NewVote vote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return ResponseEntity.ok(newVotingService.setTempVote(vote));
    }

    @PostMapping(value="/applyTempVote")
    public ResponseEntity applyTempVote(@RequestBody NewVote tempVote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return ResponseEntity.ok(newVotingService.applyTempVote(tempVote));
    }

    @PostMapping(value="/receiveVoteAnswer")
    public ResponseEntity<SingleVote> receiveVote(@RequestBody Object voteStr){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        return newVotingService.getVoteAnswer(voteStr);
    }

    @PutMapping(value="/applyVote")
    public ResponseEntity applyVote(@RequestBody NewVote vote){
        logger.info("got in :" + Thread.currentThread().getStackTrace()[0]);
        newVotingService.applyVote(vote);

        return ResponseEntity.ok(null);
    }

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public String greeting(String message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new String("LL");
//    }

}
