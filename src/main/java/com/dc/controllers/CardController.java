package com.dc.controllers;

import com.dc.pojo.Card;
import com.dc.pojo.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    RestTemplate restTemplate;

    @Autowired
    DeviceManager deviceManager;

    @RequestMapping(value="/playCard",method = RequestMethod.POST)
    public void playCard(@RequestBody Card card){
        String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";

        try {
            restTemplate.postForEntity(uri, card.getCardSign(), Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }
//        votingManager.createVote(card.getCardSign());
//        System.out.println(card.getCardValue() + " " + card.getCardSign());
    }

}
