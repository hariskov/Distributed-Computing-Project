package com.dc.controllers;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.Card;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.NewVotingManager;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xumepa on 10/9/17.
 */

@Controller
@RequestMapping(value="card")
public class CardController {

    @Autowired
    CustomRestTemplate restTemplate;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingManager votingManager;

    @RequestMapping(value="/playCard",method = RequestMethod.POST)
    public void playCard(@RequestBody Card card){
        String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";

        try {
            restTemplate.postForEntity(uri, card, Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/getPlayedCard",method = RequestMethod.POST)
    public ResponseEntity playedCard(@RequestBody Object cardStr){
        return ResponseEntity.ok(votingManager.getTempVote().getVoteStr()); // should be changed ->
    }
}
