package com.dc.controllers;

import com.dc.pojo.Vote;
import com.dc.services.NewVotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/voting")
public class MessagingController {

    @Autowired
    NewVotingService newVotingService;

    @PostMapping
    public void startVote(@RequestBody String newVote){
        Vote vote = newVotingService.creatVote(newVote);
        newVotingService.sendVote(vote);
    }

    @PostMapping("/receiveNewVote")
    public ResponseEntity sendIt(@RequestBody Vote vote){
        return ResponseEntity.ok(newVotingService.process(vote));
    }

//    @PostMapping("/receiveInfo")
//    public ResponseEntity receiveInfo(){
//
//    }
}
