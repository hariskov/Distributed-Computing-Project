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
    NewVotingManager newVotingManager;

    @RequestMapping(value="/playCard",method = RequestMethod.POST)
    public ResponseEntity playCard(@RequestBody Card card){
        String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";

        try {
            restTemplate.postForEntity(uri, card.getCardSign(), Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }
}
