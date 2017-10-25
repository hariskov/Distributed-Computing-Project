package com.dc.controllers;

import com.dc.pojo.Vote;
import com.dc.services.NewVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping(value = "/voting")
public class MessagingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewVotingService newVotingService;

    @PostMapping(value="/startVote")
    public void startVote(@RequestBody String newVote){
        logger.info("got in :" + Arrays.toString(Thread.getAllStackTraces().get(0)));
        Vote vote = newVotingService.creatVote(newVote);
        newVotingService.sendVote(vote);
    }

    @PostMapping("/receiveNewVote")
    public ResponseEntity sendIt(@RequestBody Vote vote){
        logger.info("got in :" + Arrays.toString(Thread.getAllStackTraces().get(0)));
        return ResponseEntity.ok(newVotingService.process(vote));
    }

//    @PostMapping("/receiveInfo")
//    public ResponseEntity receiveInfo(){
//
//    }
}
